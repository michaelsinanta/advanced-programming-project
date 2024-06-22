package id.ac.ui.cs.advprog.bayarservice.service.invoice;

import id.ac.ui.cs.advprog.bayarservice.dto.invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface InvoiceService {
    Invoice findById(Integer invoiceId);
    Invoice findBySessionId(UUID seessionId);
    Invoice create(InvoiceRequest request);
}
