package id.ac.ui.cs.advprog.bayarservice.service.payment;

import id.ac.ui.cs.advprog.bayarservice.core.*;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.DetailPaymentLogResponse;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.PaymentLogDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankNotSelectedException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyPaidException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentStatus;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import id.ac.ui.cs.advprog.bayarservice.repository.PaymentRepository;
import id.ac.ui.cs.advprog.bayarservice.rest.WarnetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;

    private final InvoiceRepository invoiceRepository;

    private final BankRepository bankRepository;

    private final PaymentReceiver paymentReceiver;

    private final WarnetService warnetService;

    public List<String> getPaymentMethods() {
        return Stream.of(PaymentMethod.values())
                                            .map(PaymentMethod::name)
                                            .toList();
    }

    @Override
    public PaymentLog create(Integer invoiceId, PaymentRequest request) {
        Invoice invoice = this.invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(invoiceId));
        if (invoice.getPaymentStatus().equals(PaymentStatus.PAID)) {
            throw new InvoiceAlreadyPaidException(invoice.getSessionId());
        }
        if (request.getBankId() == null &&
                PaymentMethod.valueOf(request.getPaymentMethod()) == PaymentMethod.BANK) {
            throw new BankNotSelectedException();
        }
        if (request.getBankId() != null) {
            Bank bank = this.bankRepository.findById(request.getBankId())
                    .orElseThrow(() -> new BankDoesNotExistException(request.getBankId()));
            invoice.setBank(bank);
        }

        var session = this.warnetService.getSessionViaAPI(request.getSessionId());
        var noPC = session.getPc().getNoPC();
        long totalAmount = paymentReceiver.receive(request) - invoice.getDiscount();
        invoice.setPaymentStatus(PaymentStatus.PAID);
        invoice.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));

        this.invoiceRepository.save(invoice);
        PaymentLog paymentLog = PaymentLog.builder()
                .sessionId(request.getSessionId())
                .paymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()))
                .noPC(noPC)
                .totalAmount(totalAmount < 0 ? 0 : totalAmount)
                .invoice(invoice)
                .build();
        return this.paymentRepository.save(paymentLog);
    }

    @Override
    public List<PaymentLog> getPaymentLog() {
        return this.paymentRepository.findAll();
    }

    @Override
    public List<PaymentLog> getPaymentLogByYearAndMonth(int year, int month) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year &&
                        paymentHistory.getCreatedAt().toLocalDate().getMonthValue() == month)
                .toList();
    }

    @Override
    public List<PaymentLog> getPaymentLogByYear(int year) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year)
                .toList();
    }

    @Override
    public List<PaymentLog> getPaymentLogByWeekAndYear(int year, int week) {
        List<PaymentLog> paymentHistories = this.paymentRepository.findAll();
        return paymentHistories.stream()
                .filter(paymentHistory -> paymentHistory.getCreatedAt().toLocalDate().getYear() == year &&
                        paymentHistory.getCreatedAt().toLocalDate().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()) == week)
                .toList();
    }

    @Override
    public DetailPaymentLogResponse getPaymentLogDetail(UUID sessionId) {
        PaymentLog paymentLog = this.paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new PaymentLogDoesNotExistException(sessionId));
        Invoice invoice = this.invoiceRepository.findBySessionId(sessionId).
                orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));

        Bank bank = null;
        if (invoice.getPaymentMethod().equals(PaymentMethod.BANK)) {
            bank = invoice.getBank();
        }

        return DetailPaymentLogResponse.builder()
                .paymentLog(paymentLog)
                .invoice(invoice)
                .bank(bank)
                .build();
    }
}
