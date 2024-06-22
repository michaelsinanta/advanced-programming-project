package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentHandlerRegistry {
    private final List<PaymentStrategy> requestHandlers;

    public Optional<PaymentStrategy> getHandlerFor(PaymentRequest request) {
        return requestHandlers.stream()
                .filter(handler -> {
                    try {
                        return handler.supportedPaymentMethodType().
                                equals(PaymentMethod.valueOf(request.getPaymentMethod()));
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                }).findAny();
    }
}
