package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PricingDoesNotExistException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCExistenceValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PricingExistenceValidationHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingExistenceValidationHandlerTest {

    @Mock
    private PricingRepository pricingRepository;

    @InjectMocks
    private PricingExistenceValidationHandler handler;

    CreateSessionRequest createSessionRequest;
    Pricing pricing;

    @BeforeEach
    void setUp(){
        createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setPricingId(1);
        pricing = new Pricing();
    }

    @Test
    void testHandleRequestPricingExists() {
        when(pricingRepository.findById(anyInt())).thenReturn(Optional.of(pricing));

        assertDoesNotThrow(() -> {
            handler.handleRequest(createSessionRequest);
        });
    }

    @Test
    void testHandleRequestPricingDoesNotExist() {
        when(pricingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(PricingDoesNotExistException.class, () -> {
            handler.handleRequest(createSessionRequest);
        });
    }
}
