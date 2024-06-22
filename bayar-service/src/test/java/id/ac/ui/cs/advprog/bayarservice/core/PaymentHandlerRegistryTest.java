package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class PaymentHandlerRegistryTest {

    @Autowired
    private PaymentHandlerRegistry paymentHandlerRegistry;

    @Test
    void testPaymentHandlerRegustryShouldReturnPaymentStrategy() {
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .paymentMethod(String.valueOf(PaymentMethod.CASH))
                .build();

        Optional<PaymentStrategy> paymentStrategy =
                paymentHandlerRegistry.getHandlerFor(paymentRequest);

        Assertions.assertNotNull(paymentStrategy);
    }
}
