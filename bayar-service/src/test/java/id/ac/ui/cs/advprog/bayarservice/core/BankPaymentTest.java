package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.repository.BankRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankPaymentTest {

    @Autowired
    BankPayment bankPayment;

    @Autowired
    BankRepository bankRepository;

    Bank bank;
    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        bank = Bank.builder()
                .name("BNI")
                .adminFee(5000)
                .build();
        bankRepository.save(bank);
    }

    @Test
    void testBankPaymentShouldReturnCorrectTotalAmount() {
        paymentRequest = PaymentRequest.builder()
                .totalAmount(50000L)
                .bankId(bank.getId())
                .build();
        long totalAmount = bankPayment.handle(paymentRequest);

        Assertions.assertEquals(55000, totalAmount);
    }

    @Test
    void testBankPaymentShouldReturn404() {
        paymentRequest = PaymentRequest.builder()
                .totalAmount(50000L)
                .bankId(100)
                .build();

        Assertions.assertThrows(
                BankDoesNotExistException.class,
                () -> bankPayment.handle(paymentRequest)
        );
    }

    @Test
    void testSupportedPaymentMethodTypeShouldReturnBank() {
        PaymentMethod paymentMethod = bankPayment.supportedPaymentMethodType();

        Assertions.assertEquals(PaymentMethod.BANK, paymentMethod);
    }
}
