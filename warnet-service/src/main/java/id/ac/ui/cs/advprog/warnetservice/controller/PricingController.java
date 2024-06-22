package id.ac.ui.cs.advprog.warnetservice.controller;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.PricingByPCResponse;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.PricingServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/warnet/sewa_pc")
@RequiredArgsConstructor
public class PricingController {
    private final PricingServiceImpl pricingService;
    @PostMapping("/create_session")
    public ResponseEntity<CreateSessionResponse> createSession(@RequestBody CreateSessionRequest createSessionRequest) {
        CreateSessionResponse response = pricingService.createSession(createSessionRequest);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get_session/{sessionId}")
    public ResponseEntity<Session> getSession(@PathVariable String sessionId) {
        UUID sessionUUID = UUID.fromString(sessionId);
        Session response = pricingService.getSession(sessionUUID);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get_pricing_by_pc/{pcId}/{isPaket}")
    public ResponseEntity<List<PricingByPCResponse>> getAllPricingByPC(@PathVariable Integer pcId, @PathVariable Boolean isPaket) {
        List<PricingByPCResponse> response = pricingService.getAllPricingbyPC(pcId, isPaket);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/session_done/{sessionId}")
    public ResponseEntity<Session> sessionDone(@PathVariable String sessionId) {
        UUID sessionUUID = UUID.fromString(sessionId);
        Session response = pricingService.endSession(sessionUUID);
        return ResponseEntity.ok(response);
    }

}