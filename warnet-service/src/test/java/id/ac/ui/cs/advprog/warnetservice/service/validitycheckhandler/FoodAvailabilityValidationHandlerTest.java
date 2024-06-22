package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.MenuItemDTO;
import id.ac.ui.cs.advprog.warnetservice.exceptions.FoodSoldOutException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.rest.CafeService;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.FoodAvailabilityValidationHandler;
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
class FoodAvailabilityValidationHandlerTest {

    @Mock
    private PricingRepository pricingRepository;

    @Mock
    private CafeService cafeService;
    @InjectMocks
    private FoodAvailabilityValidationHandler handler;

    CreateSessionRequest createSessionRequest;
    Pricing pricing;

    MenuItemDTO menuItemDTOEmpty, menuItemDTOAvailable;

    @BeforeEach
    void setUp(){
        createSessionRequest = new CreateSessionRequest();
        createSessionRequest.setPricingId(1);
        pricing = new Pricing();

        pricing.setIsPaket(true);
        pricing.setId(1);
        pricing.setMakananId("896b256f-aaae-46e4-8933-e8310c86dd3f");

        menuItemDTOAvailable = new MenuItemDTO();
        menuItemDTOAvailable.setStock(10);

        menuItemDTOEmpty = new MenuItemDTO();
        menuItemDTOEmpty.setStock(0);
    }

    @Test
    void testHandleRequestFoodExists() {
        when(pricingRepository.findById(anyInt())).thenReturn(Optional.of(pricing));
        when(cafeService.getMenuItemByIdFromMicroservice(anyString())).thenReturn(menuItemDTOAvailable);

        assertDoesNotThrow(() -> {
            handler.handleRequest(createSessionRequest);
        });

        verify(cafeService, times(1)).getMenuItemByIdFromMicroservice("896b256f-aaae-46e4-8933-e8310c86dd3f");
        verify(pricingRepository, times(1)).findById(1);
    }

    @Test
    void testHandleRequestFoodSoldOut() {
        when(pricingRepository.findById(anyInt())).thenReturn(Optional.of(pricing));
        when(cafeService.getMenuItemByIdFromMicroservice(anyString())).thenReturn(menuItemDTOEmpty);

        assertThrows(FoodSoldOutException.class, () -> {
            handler.handleRequest(createSessionRequest);
        });
        verify(pricingRepository, times(1)).findById(1);
    }

    @Test
    void testPricingNotExist() {
        when(pricingRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            handler.handleRequest(createSessionRequest);
        });
        verify(pricingRepository, times(1)).findById(1);
        verify(cafeService, times(0)).getMenuItemByIdFromMicroservice(anyString());
    }
}
