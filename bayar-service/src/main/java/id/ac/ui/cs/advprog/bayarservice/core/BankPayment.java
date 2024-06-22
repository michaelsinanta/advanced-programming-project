package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import org.springframework.stereotype.Component;

@Component
public class BankPayment implements PaymentStrategy {
    private final BankRepository bankRepository;

    public BankPayment(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public long handle(PaymentRequest request) {
        Bank bank = this.bankRepository.findById(request.getBankId())
                .orElseThrow(() -> new BankDoesNotExistException(request.getBankId()));
        return request.getTotalAmount() + bank.getAdminFee();
    }

    @Override
    public PaymentMethod supportedPaymentMethodType() {
        return PaymentMethod.BANK;
    }
}
