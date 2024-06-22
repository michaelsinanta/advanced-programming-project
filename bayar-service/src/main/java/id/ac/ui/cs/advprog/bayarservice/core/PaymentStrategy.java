package id.ac.ui.cs.advprog.bayarservice.core;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;

public interface PaymentStrategy {
    long handle(PaymentRequest request);

    PaymentMethod supportedPaymentMethodType();
}
