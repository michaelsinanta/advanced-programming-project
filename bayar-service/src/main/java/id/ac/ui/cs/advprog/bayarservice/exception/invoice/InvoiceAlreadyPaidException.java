package id.ac.ui.cs.advprog.bayarservice.exception.invoice;

import java.util.UUID;

public class InvoiceAlreadyPaidException extends RuntimeException {
    public InvoiceAlreadyPaidException(UUID sessionId) {
        super("Invoice with sessionId " + sessionId + " already paid");
    }
}
