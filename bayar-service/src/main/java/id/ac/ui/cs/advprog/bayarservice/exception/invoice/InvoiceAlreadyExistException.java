package id.ac.ui.cs.advprog.bayarservice.exception.invoice;

import java.util.UUID;

public class InvoiceAlreadyExistException extends RuntimeException {
    public InvoiceAlreadyExistException(UUID sessionId) {
        super("Invoice with sessionId " + sessionId + " already exist");
    }
}
