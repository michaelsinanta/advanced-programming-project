package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCPricingPairDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;

@Component
public class PCPricingPairValidationHandler extends AbstractValidityCheckHandler {
    @Autowired
    PCRepository pcRepository;

    @Autowired
    PricingRepository pricingRepository;

    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Optional<PC> optionalPc = pcRepository.findById(createSessionRequest.getPcId());
        Optional<Pricing> optionalPricing = pricingRepository.findById(createSessionRequest.getPricingId());

        PC pc = optionalPc.orElseGet(PC::new);
        Pricing pricing = optionalPricing.orElseGet(Pricing::new);

        if (!pc.getPricingList().contains(pricing)) {
            throw new PCPricingPairDoesNotExistException(pc.getId(), pricing.getId());
        }

        callNextHandler(createSessionRequest);
    }
}
