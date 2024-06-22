package id.ac.ui.cs.advprog.bayarservice.service.invoice;

import id.ac.ui.cs.advprog.bayarservice.dto.invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;

    @Override
    public Invoice findById(Integer id) {
        Optional<Invoice> invoice = invoiceRepository.findById(id);
        if (invoice.isEmpty()) {
            throw new InvoiceDoesNotExistException(id);
        }
        return invoice.get();
    }

    @Override
    public Invoice findBySessionId(UUID sessionId) {
        return this.invoiceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));
    }

    @Override
    public Invoice create(InvoiceRequest request) {
        if (this.invoiceRepository.findBySessionId(request.getSessionId()).isPresent()) {
            throw new InvoiceAlreadyExistException(request.getSessionId());
        }
        return invoiceRepository.save(request.toEntity());
    }
}