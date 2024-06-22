package id.ac.ui.cs.advprog.bayarservice.exception.invoice;

import java.util.UUID;

public class InvoiceDoesNotExistException extends RuntimeException {
    public InvoiceDoesNotExistException(Integer invoiceId) {
        super("Invoice with id " + invoiceId + " does not exist");
    }

    public InvoiceDoesNotExistException(UUID sessionId) {
        super("Invoice with sessionId " + sessionId + " does not exist");
    }
}

