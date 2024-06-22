package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PricingDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;

@Component
public class PricingExistenceValidationHandler extends AbstractValidityCheckHandler {
    @Autowired
    PricingRepository pricingRepository;

    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Optional<Pricing> optionalPricing = pricingRepository.findById(createSessionRequest.getPricingId());
        if (optionalPricing.isEmpty()) {
            throw new PricingDoesNotExistException(createSessionRequest.getPricingId());
        }

        callNextHandler(createSessionRequest);
    }
}
