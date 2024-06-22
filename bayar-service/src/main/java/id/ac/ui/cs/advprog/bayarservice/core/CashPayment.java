package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class CashPayment implements PaymentStrategy {
    @Override
    public long handle(PaymentRequest request) {
        return request.getTotalAmount();
    }

    @Override
    public PaymentMethod supportedPaymentMethodType()  {
        return PaymentMethod.CASH;
    }
}
