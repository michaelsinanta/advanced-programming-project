package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCUsageValidationHandler;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PCUsageValidationHandlerTest {

    @InjectMocks
    private PCUsageValidationHandler handler;

    @Mock
    private PCRepository pcRepository;

    @Mock
    private KelolaWarnetServiceImpl kelolaWarnetService;

     @Test
     void testHandleRequestWhenPCIsNotBeingUsed() {
         CreateSessionRequest request = CreateSessionRequest.builder().pcId(1).build();

         PC pc = PC.builder().id(1).build();
         when(pcRepository.findById(1)).thenReturn(Optional.of(pc));
         when(kelolaWarnetService.isPCBeingUsed(1)).thenReturn(false);

         handler.handleRequest(request);

         verify(pcRepository, times(1)).findById(1);
         verify(kelolaWarnetService, times(1)).isPCBeingUsed(1);
     }

     @Test
     void testHandleRequestWhenPCIsBeingUsed() {
         CreateSessionRequest request = CreateSessionRequest.builder().pcId(1).build();

         PC pc = PC.builder().id(1).build();
         when(pcRepository.findById(1)).thenReturn(Optional.of(pc));
         when(kelolaWarnetService.isPCBeingUsed(1)).thenReturn(true);

         assertThrows(PCIsBeingUsedException.class, () -> handler.handleRequest(request));

         verify(pcRepository, times(1)).findById(1);
         verify(kelolaWarnetService, times(1)).isPCBeingUsed(1);
     }
}

