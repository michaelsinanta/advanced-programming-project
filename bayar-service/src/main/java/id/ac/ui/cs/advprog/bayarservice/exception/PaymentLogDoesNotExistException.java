package id.ac.ui.cs.advprog.bayarservice.exception;

import java.util.UUID;

public class PaymentLogDoesNotExistException extends RuntimeException {
    public PaymentLogDoesNotExistException(UUID sessionId) {
        super("Payment Log with sessionId " + sessionId + " does not exist");
    }
}
