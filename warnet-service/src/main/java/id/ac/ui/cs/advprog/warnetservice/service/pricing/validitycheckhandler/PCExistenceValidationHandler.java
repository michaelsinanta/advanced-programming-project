package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;

@Component
public class PCExistenceValidationHandler extends AbstractValidityCheckHandler {
    @Autowired
    PCRepository pcRepository;

    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Optional<PC> optionalPc = pcRepository.findById(createSessionRequest.getPcId());
        if (optionalPc.isEmpty()) {
            throw new PCDoesNotExistException(createSessionRequest.getPcId());
        }

        callNextHandler(createSessionRequest);
    }
}

