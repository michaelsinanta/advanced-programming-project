package id.ac.ui.cs.advprog.warnetservice.service.pricing;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.PricingByPCResponse;
import id.ac.ui.cs.advprog.warnetservice.model.Session;

@Service
public interface PricingService {
    CreateSessionResponse createSession(CreateSessionRequest createSessionRequest);
    Session getSession(UUID id);
    List<PricingByPCResponse> getAllPricingbyPC(Integer pcId, Boolean reqIsPaket);
}