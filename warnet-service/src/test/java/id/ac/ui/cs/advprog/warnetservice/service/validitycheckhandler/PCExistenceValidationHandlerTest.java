package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCExistenceValidationHandler;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PCExistenceValidationHandlerTest {

    @Mock
    private PCRepository pcRepository;

    @InjectMocks
    private PCExistenceValidationHandler handler;

    CreateSessionRequest createSessionRequest;
    PC pc;

    @BeforeEach
    void setUp(){
        createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setPcId(1);
        pc = new PC();
    }

    @Test
    void testHandleRequestPCExists() {
        when(pcRepository.findById(anyInt())).thenReturn(Optional.of(pc));

        assertDoesNotThrow(() -> {
            handler.handleRequest(createSessionRequest);
        });
    }

    @Test
    void testHandleRequestPCDoesNotExist() {
        when(pcRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(PCDoesNotExistException.class, () -> {
            handler.handleRequest(createSessionRequest);
        });
    }
}
