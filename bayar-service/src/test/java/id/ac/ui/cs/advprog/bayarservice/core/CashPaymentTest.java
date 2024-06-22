package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CashPaymentTest {

    @Autowired
    CashPayment cashPayment;

    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        paymentRequest = PaymentRequest.builder()
                .totalAmount(50000L)
                .build();
    }

    @Test
    void testBankPaymentShouldReturnCorrectTotalAmount() {
        long totalAmount = cashPayment.handle(paymentRequest);

        Assertions.assertEquals(50000L, totalAmount);
    }

    @Test
    void testSupportedPaymentMethodTypeShouldReturnCash() {
        PaymentMethod paymentMethod = cashPayment.supportedPaymentMethodType();

        Assertions.assertEquals(PaymentMethod.CASH, paymentMethod);
    }
}
