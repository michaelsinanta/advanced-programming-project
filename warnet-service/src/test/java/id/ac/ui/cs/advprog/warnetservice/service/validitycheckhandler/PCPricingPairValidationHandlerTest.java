package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCPricingPairDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCPricingPairValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCUsageValidationHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PCPricingPairValidationHandlerTest {

    @InjectMocks
    private PCPricingPairValidationHandler handler;

    @Mock
    private PCRepository pcRepository;

    @Mock
    private PricingRepository pricingRepository;

    @Test
    void testHandleRequestWhenPairIsValid() {
        CreateSessionRequest request = CreateSessionRequest.builder().pcId(1).pricingId(2).build();
        PC pc = PC.builder().id(1).build();
        Pricing pricing = Pricing.builder().id(2).build();
        pc.setPricingList(List.of(pricing));

        when(pcRepository.findById(1)).thenReturn(Optional.of(pc));
        when(pricingRepository.findById(2)).thenReturn(Optional.of(pricing));


        handler.handleRequest(request);

        verify(pcRepository, times(1)).findById(1);
        verify(pricingRepository, times(1)).findById(2);
    }

    @Test
    void testHandleRequestWhenPairIsInvalid() {
        CreateSessionRequest request = CreateSessionRequest.builder().pcId(1).pricingId(3).build();
        PC pc = PC.builder().id(1).build();
        Pricing pricing = Pricing.builder().id(3).build();
        pc.setPricingList(Collections.emptyList());

        when(pcRepository.findById(1)).thenReturn(Optional.of(pc));
        when(pricingRepository.findById(3)).thenReturn(Optional.of(pricing));

        assertThrows(PCPricingPairDoesNotExistException.class, () -> handler.handleRequest(request));

        verify(pcRepository, times(1)).findById(1);
        verify(pricingRepository, times(1)).findById(3);
    }
}
