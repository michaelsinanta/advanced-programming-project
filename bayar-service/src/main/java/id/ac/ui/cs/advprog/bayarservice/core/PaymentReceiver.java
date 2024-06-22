package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvalidPaymentMethodException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentReceiver {
    private final PaymentHandlerRegistry registry;

    public long receive(PaymentRequest request) {
        return registry.getHandlerFor(request)
                .orElseThrow(() -> new InvalidPaymentMethodException(request.getPaymentMethod()))
                .handle(request);
    }
}
