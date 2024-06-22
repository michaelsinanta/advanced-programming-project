package id.ac.ui.cs.advprog.bayarservice.service;

import id.ac.ui.cs.advprog.bayarservice.core.PaymentReceiver;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.DetailPaymentLogResponse;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.warnet.PCResponse;
import id.ac.ui.cs.advprog.bayarservice.dto.warnet.SessionResponse;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
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
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BankRepository bankRepository;

    @Mock
    private PaymentReceiver paymentReceiver;

    @Mock
    private WarnetService warnetService;

    SessionResponse sessionResponse;
    PCResponse pc;
    PaymentRequest createRequest;
    PaymentLog paymentLog;
    Invoice invoice;
    Bank bank;
    UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        invoice = Invoice.builder()
                .paymentStatus(PaymentStatus.UNPAID)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .build();

        createRequest = PaymentRequest.builder()
                .totalAmount(100000L)
                .sessionId(uuid)
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        paymentLog = PaymentLog.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .createdAt(Date.valueOf(LocalDate.now()))
                .sessionId(uuid)
                .invoice(invoice)
                .noPC(1)
                .build();

        bank = Bank.builder()
                .id(1)
                .name("BNI")
                .adminFee(5000)
                .build();

        pc = PCResponse.builder()
                .noPC(1)
                .build();

        sessionResponse = SessionResponse.builder()
                .pc(pc)
                .build();
    }

    @Test
    void whenGetPaymentMethodShouldReturnList() {
        List<String> result = paymentService.getPaymentMethods();
        List<String> paymentMethods = Stream.of(PaymentMethod.values())
                .map(PaymentMethod::name)
                .collect(Collectors.toList());

        Assertions.assertEquals(result, paymentMethods);
    }

    @Test
    void whenCreatePaymentShouldReturnTheCreatedPaymentHistory() {
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any(PaymentLog.class))).thenAnswer(invocation -> {
            var pL = invocation.getArgument(0, PaymentLog.class);
            pL.setId(1);
            pL.setTotalAmount(paymentLog.getTotalAmount());
            pL.setCreatedAt(paymentLog.getCreatedAt());
            return pL;
        });

        when(warnetService.getSessionViaAPI(any())).thenReturn(sessionResponse);

        PaymentLog result = paymentService.create(invoice.getId(), createRequest);

        verify(paymentRepository, atLeastOnce()).save(any(PaymentLog.class));
        Assertions.assertEquals(paymentLog, result);
    }

    @Test
    void whenCreatePaymentWithBankShouldReturnTheCreatedPaymentHistory() {
        createRequest.setPaymentMethod(String.valueOf(PaymentMethod.BANK));
        createRequest.setBankId(bank.getId());
        paymentLog.setPaymentMethod(PaymentMethod.BANK);
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any(PaymentLog.class))).thenAnswer(invocation -> {
            var pL = invocation.getArgument(0, PaymentLog.class);
            pL.setId(1);
            pL.setTotalAmount(paymentLog.getTotalAmount());
            pL.setCreatedAt(paymentLog.getCreatedAt());
            return pL;
        });

        when(warnetService.getSessionViaAPI(any())).thenReturn(sessionResponse);
        when(bankRepository.findById(any())).thenReturn(Optional.of(bank));

        PaymentLog result = paymentService.create(invoice.getId(), createRequest);

        verify(paymentRepository, atLeastOnce()).save(any(PaymentLog.class));
        Assertions.assertEquals(paymentLog, result);
    }

    @Test
    void whenCreatePaymentShouldReturn404InvoiceNotFound() {
        when(invoiceRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(InvoiceDoesNotExistException.class,
                () -> paymentService.create(0, createRequest));
    }

    @Test
    void whenCreatePaymentAndInvoiceAlreadyExistShouldReturn400BadRequest() {
        invoice.setPaymentStatus(PaymentStatus.PAID);
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));

        Assertions.assertThrows(InvoiceAlreadyPaidException.class,
                () -> paymentService.create(0, createRequest));
    }

    @Test
    void whenCreatePaymentAndBankNotSelectedAndPaymentMethodIsBankShouldReturn400BadRequest() {
        createRequest.setPaymentMethod(String.valueOf(PaymentMethod.BANK));
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));

        Assertions.assertThrows(BankNotSelectedException.class,
                () -> paymentService.create(0, createRequest));
    }

    @Test
    void whenCreatePaymentShouldAndBankNotFoundReturn404NotFound() {
        createRequest.setPaymentMethod(String.valueOf(PaymentMethod.BANK));
        createRequest.setBankId(100);
        when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
        when(bankRepository.findById(any())).thenReturn(Optional.empty());

        Assertions.assertThrows(BankDoesNotExistException.class,
                () -> paymentService.create(0, createRequest));
    }

    @Test
    void whenGetPaymentLogShouldReturnListOfAllPaymentLog() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        List<PaymentLog> result = paymentService.getPaymentLog();

        Assertions.assertEquals(List.of(paymentLog), result);
    }

    @Test
    void whenGetPaymentLogByYearShouldReturnListOfPaymentLogByYear() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        int year = LocalDate.now().getYear();

        List<PaymentLog> result = paymentService.getPaymentLogByYear(year);

        Assertions.assertEquals(List.of(paymentLog), result);
    }

    @Test
    void whenGetPaymentLogByYearNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        List<PaymentLog> result = paymentService.getPaymentLogByYear(2021);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void whenGetPaymentLogByMonthShouldReturnListOfPaymentLogByMonth() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();

        List<PaymentLog> result = paymentService.getPaymentLogByYearAndMonth(year, month);

        Assertions.assertEquals(List.of(paymentLog), result);
    }

    @Test
    void whenGetPaymentLogByMonthNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        List<PaymentLog> result = paymentService.getPaymentLogByYearAndMonth(2020, 2);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void whenGetPaymentLogByWeekAndYearShouldReturnListOfPaymentLogByWeekAndYear() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        int year = LocalDate.now().getYear();
        int week = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());

        List<PaymentLog> result = paymentService.getPaymentLogByWeekAndYear(year, week);

        Assertions.assertEquals(List.of(paymentLog), result);
    }

    @Test
    void whenGetPaymentLogByWeekAndYearNotFoundShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of(paymentLog));

        List<PaymentLog> result = paymentService.getPaymentLogByWeekAndYear(2020, 2);

        Assertions.assertEquals(List.of(), result);
    }

    @Test
    void whenGetPaymentLogDetailShouldReturnDetailPaymentLogResponse() {
        when(paymentRepository.findBySessionId(any())).thenReturn(Optional.of(paymentLog));
        invoice.setPaymentMethod(PaymentMethod.BANK);
        invoice.setBank(bank);
        when(invoiceRepository.findBySessionId(any())).thenReturn(Optional.of(invoice));

        DetailPaymentLogResponse detailPaymentLogResponse = DetailPaymentLogResponse.builder()
                .invoice(invoice)
                .paymentLog(paymentLog)
                .bank(bank)
                .build();

        DetailPaymentLogResponse result = paymentService.getPaymentLogDetail(uuid);

        Assertions.assertEquals(detailPaymentLogResponse, result);
    }
}
