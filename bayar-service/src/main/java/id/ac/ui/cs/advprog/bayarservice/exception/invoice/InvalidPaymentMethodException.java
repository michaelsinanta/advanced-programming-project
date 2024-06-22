package id.ac.ui.cs.advprog.bayarservice.exception.invoice;

public class InvalidPaymentMethodException extends RuntimeException {
    public InvalidPaymentMethodException(String paymentMethod) {
        super("Invalid paymentMethod: " + paymentMethod);
    }
}