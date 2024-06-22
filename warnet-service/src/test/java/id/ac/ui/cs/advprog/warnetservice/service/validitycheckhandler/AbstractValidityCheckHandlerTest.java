package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetService;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.AbstractValidityCheckHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.FoodAvailabilityValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCUsageValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.QuantityValidationHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AbstractValidityCheckHandlerTest {
    @Mock
    private AbstractValidityCheckHandler nextHandler;

    private AbstractValidityCheckHandler handler;

    @BeforeEach
    void setUp() {
        handler = new AbstractValidityCheckHandler() {
            @Override
            public void handleRequest(CreateSessionRequest createSessionRequest) {
                Integer quantity = createSessionRequest.getQuantity();

                if (quantity <= 0) {
                    throw new NonPositiveParameterException("quantity");
                }

                callNextHandler(createSessionRequest);
            }
        };
    }

    @Test
    void testSetNextHandler() {
        AbstractValidityCheckHandler returnedHandler = handler.setNextHandler(nextHandler);

        assertEquals(nextHandler, returnedHandler);
    }

    @Test
    void testCallNextHandler() {
        CreateSessionRequest request = new CreateSessionRequest();
        handler.setNextHandler(nextHandler);

        handler.callNextHandler(request);

        verify(nextHandler).handleRequest(request);
    }
}
