package id.ac.ui.cs.advprog.warnetservice.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.ExtendSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.FoodSoldOutException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCPricingPairDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PricingDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionExpiredException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.service.perpanjang.PerpanjangServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.PricingServiceImpl;

@ExtendWith(MockitoExtension.class)
class PerpanjangServiceImplTest{
    @InjectMocks
    private PerpanjangServiceImpl service;

    @Mock
    private PricingServiceImpl pricingServiceImpl;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private PricingRepository pricingRepository;

    @Mock
    private SessionPricingRepository sessionPricingRepository;

    Session session;
    PC testPC;
    LocalDateTime initialTime;
    Pricing testPricing;
    ExtendSessionRequest extendSessionRequest;
    
    @BeforeEach
    void setUp(){
        initialTime = LocalDateTime.now().minusHours(2);
        testPC = PC.builder()
                .noPC(1)
                .noRuangan(1)
                .build();
        testPC.setId(1);

        testPricing = Pricing.builder()
                .id(2)
                .name("Tarif 1")
                .price(1)
                .duration(1)
                .makananId(null)
                .isPaket(false)
                .pcList(List.of(testPC))
                .build();

        testPC.setPricingList(Arrays.asList(testPricing));

        session = Session.builder()
                .pc(testPC)
                .datetimeStart(initialTime)
                .datetimeEnd(initialTime.plusHours(5))
                .build();
        session.setId(UUID.randomUUID());

        extendSessionRequest = new ExtendSessionRequest(session.getId(), testPricing.getId(), 3);
    }

    @Test
    void testExtendSessionSuccess(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(pricingRepository.findById(testPricing.getId())).thenReturn(Optional.of(testPricing));

        when(pricingServiceImpl.isFoodStockAvailable(testPricing.getMakananId())).thenReturn(true);

        when(sessionRepository.save(any(Session.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Session response = service.extendSession(extendSessionRequest);
        session.setDatetimeEnd(initialTime.plusHours(5));
        assertEquals(session, response);
    }

    @Test
    void testExtendSessionFailSessionDoesNotExist(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.empty());

        assertThrows(SessionDoesNotExistException.class, () -> service.extendSession(extendSessionRequest));
    }

    @Test
    void testExtendSessionFailSessionExpired(){
        session.setDatetimeEnd(initialTime.plusHours(1));
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        
        assertThrows(SessionExpiredException.class, () -> service.extendSession(extendSessionRequest));
    }

    @Test
    void testExtendSessionFailPricingDoesNotExist(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(pricingRepository.findById(testPricing.getId())).thenReturn(Optional.empty());

        assertThrows(PricingDoesNotExistException.class, () -> service.extendSession(extendSessionRequest));
    }

    @Test
    void testExtendSessionFailPCPricingPairDoesNotExist(){
        testPC.setPricingList(Collections.emptyList());
        session.setPc(testPC);
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(pricingRepository.findById(testPricing.getId())).thenReturn(Optional.of(testPricing));

        assertThrows(PCPricingPairDoesNotExistException.class, () -> service.extendSession(extendSessionRequest));
    }

    @Test
    void testExtendSessionFailNonPositiveQuantity(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(pricingRepository.findById(testPricing.getId())).thenReturn(Optional.of(testPricing));
        extendSessionRequest.setQuantity(0);

        assertThrows(NonPositiveParameterException.class, () -> service.extendSession(extendSessionRequest));
    }

    @Test
    void testExtendSessionFailFoodSoldOut(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        when(pricingRepository.findById(testPricing.getId())).thenReturn(Optional.of(testPricing));

        when(pricingServiceImpl.isFoodStockAvailable(testPricing.getMakananId())).thenReturn(false);

        assertThrows(FoodSoldOutException.class, () -> service.extendSession(extendSessionRequest));
    }
}
