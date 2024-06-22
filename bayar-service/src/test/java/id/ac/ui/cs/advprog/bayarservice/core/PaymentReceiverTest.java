package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvalidPaymentMethodException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PaymentReceiverTest {

    @Autowired
    private PaymentReceiver paymentReceiver;

    @Test
    void testPaymentReceiverShouldReturnAmountBasedOnPaymentMethod() {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(50000L)
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        long amount = paymentReceiver.receive(paymentRequest);

        Assertions.assertEquals(50000L, amount);
    }

    @Test
    void testPaymentReceiverShouldReturn400() {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .totalAmount(50000L)
                .paymentMethod("Invalid Payment Method")
                .build();

        Assertions.assertThrows(
                InvalidPaymentMethodException.class,
                () -> paymentReceiver.receive(paymentRequest)
        );
    }
}
