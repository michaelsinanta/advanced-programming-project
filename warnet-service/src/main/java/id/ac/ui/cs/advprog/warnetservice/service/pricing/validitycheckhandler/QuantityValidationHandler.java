package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;

@Component
public class QuantityValidationHandler extends AbstractValidityCheckHandler {
    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Integer quantity = createSessionRequest.getQuantity();

        if (quantity <= 0) {
            throw new NonPositiveParameterException("quantity");
        }

        callNextHandler(createSessionRequest);
    }
}
