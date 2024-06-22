package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.MenuItemDTO;
import id.ac.ui.cs.advprog.warnetservice.exceptions.FoodSoldOutException;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.rest.CafeService;

@Component
public class FoodAvailabilityValidationHandler extends AbstractValidityCheckHandler {
    @Autowired
    PricingRepository pricingRepository;

    @Autowired
    CafeService cafeService;

    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Optional<Pricing> optionalPricing = pricingRepository.findById(createSessionRequest.getPricingId());
        Pricing pricing = optionalPricing.isPresent() ? optionalPricing.get() : new Pricing();

        String makananId = pricing.getMakananId();
        if (makananId != null) {
            MenuItemDTO menuItemDTO = cafeService.getMenuItemByIdFromMicroservice(makananId);
            if (menuItemDTO.getStock() <= 0) {
                throw new FoodSoldOutException();
            }
        }

        callNextHandler(createSessionRequest);
    }
}

