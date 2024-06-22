package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;

@Component
public class PCUsageValidationHandler extends AbstractValidityCheckHandler {
    @Autowired
    PCRepository pcRepository;

    @Autowired
    KelolaWarnetServiceImpl kelolaWarnetService;

    @Override
    public void handleRequest(CreateSessionRequest createSessionRequest) {
        Optional<PC> optionalPc = pcRepository.findById(createSessionRequest.getPcId());
        PC pc = optionalPc.orElseGet(PC::new);

        if (kelolaWarnetService.isPCBeingUsed(pc.getId())) {
            throw new PCIsBeingUsedException(pc.getNoPC(), pc.getNoRuangan());
        }

        callNextHandler(createSessionRequest);
    }
}

