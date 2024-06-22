package id.ac.ui.cs.advprog.bayarservice.service.discount;

import id.ac.ui.cs.advprog.bayarservice.dto.discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.discount.DiscountAboveAHundredPercentException;
import id.ac.ui.cs.advprog.bayarservice.exception.discount.DiscountAboveTotalPriceException;
import id.ac.ui.cs.advprog.bayarservice.exception.discount.DiscountBelowAThousandException;
import id.ac.ui.cs.advprog.bayarservice.exception.discount.DiscountNegativeException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.discount.DiscountType;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DiscountServiceImpl implements  DiscountService {

    private final InvoiceRepository invoiceRepository;

    public DiscountServiceImpl(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public void giveDiscount(UUID sessionId, DiscountRequest request) {
        Invoice invoice = this.invoiceRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new InvoiceDoesNotExistException(sessionId));

        if (DiscountType.valueOf(request.getDiscountType()) == DiscountType.NOMINAL) {
            if (request.getDiscount() < 1000) {
                throw new DiscountBelowAThousandException();
            }
            else if (request.getDiscount() > (invoice.getTotalAmount() - request.getDiscount())) {
                throw new DiscountAboveTotalPriceException();
            }
            else {
                invoice.setDiscount((long) (invoice.getDiscount() + request.getDiscount()));
            }
        } else {
            if (request.getDiscount() > 100) {
                throw new DiscountAboveAHundredPercentException();
            }
            else if (request.getDiscount() < 0) {
                throw new DiscountNegativeException();
            }
            else {
                long newDiscount = (long) (((double) invoice.getTotalAmount()/100) * request.getDiscount());
                if ((invoice.getDiscount() + newDiscount) > invoice.getTotalAmount()) {
                    invoice.setDiscount(invoice.getTotalAmount());
                }
                else {
                    invoice.setDiscount(invoice.getDiscount() + newDiscount);
                }
            }
        }

        this.invoiceRepository.save(invoice);
    }
}
