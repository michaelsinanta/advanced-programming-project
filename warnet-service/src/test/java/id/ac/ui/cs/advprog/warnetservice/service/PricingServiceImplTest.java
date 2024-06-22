package id.ac.ui.cs.advprog.warnetservice.service;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.PricingByPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.BillRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.InvoiceRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.MenuItemDTO;
import id.ac.ui.cs.advprog.warnetservice.dto.rest.OrderRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.FoodSoldOutException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCInterruptedException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCPricingPairDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PricingDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionExpiredException;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.PricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.rest.BayarService;
import id.ac.ui.cs.advprog.warnetservice.rest.CafeService;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.PricingServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.FoodAvailabilityValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCExistenceValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCPricingPairValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PCUsageValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.PricingExistenceValidationHandler;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.QuantityValidationHandler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PricingServiceImplTest {
    @InjectMocks
    private PricingServiceImpl service;

    @Mock
    private KelolaWarnetServiceImpl kelolaWarnetService;

    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private PCRepository pcRepository;
    @Mock
    private SessionPricingRepository sessionPricingRepository;
    @Mock
    private PricingRepository pricingRepository;
    @Mock
    private BayarService bayarService;
    @Mock
    private CafeService cafeService;
    @Mock
    private PCExistenceValidationHandler pcExistenceValidationHandler;
    @Mock
    private PricingExistenceValidationHandler pricingExistenceValidationHandler;
    @Mock
    private PCPricingPairValidationHandler pcPricingPairValidationHandler;
    @Mock
    private PCUsageValidationHandler pcUsageValidationHandler;
    @Mock
    private QuantityValidationHandler quantityValidationHandler;
    @Mock
    private FoodAvailabilityValidationHandler foodAvailabilityValidationHandler;

    Session session, endSessionSuccess, endSessionExpired;
    PC testPC, testPC2, testPC3;
    Pricing testTarif, testPaket;
    MenuItemDTO menuItemDTO;
    LocalDateTime initialTime;
    Long duration;
    ChronoUnit unit;
    UUID validId;
    private PricingByPCResponse pricingByPCResponsePaket, pricingByPCResponseTarif;
    private CreateSessionRequest createRequest;
    private CreateSessionResponse validCreateResponse;

    @BeforeEach
    void setUp(){
        initialTime = LocalDateTime.now();
        duration = 2L;
        unit = ChronoUnit.SECONDS;
        validId = UUID.randomUUID();

        testPC = PC.builder()
                .noPC(1)
                .noRuangan(1)
                .build();
        testPC.setId(1);

        testPC2 = PC.builder()
                .noPC(2)
                .noRuangan(1)
                .build();
        testPC2.setId(2);

        testPC3 = PC.builder()
                .noPC(3)
                .noRuangan(1)
                .build();

        testPaket = Pricing.builder()
                .id(1)
                .name("Paket 1")
                .price(1)
                .duration(1)
                .makananId(UUID.randomUUID().toString())
                .isPaket(true)
                .pcList(List.of(testPC, testPC2))
                .build();

        testTarif = Pricing.builder()
                .id(2)
                .name("Tarif 1")
                .price(1)
                .duration(1)
                .makananId(null)
                .isPaket(false)
                .pcList(List.of(testPC, testPC2, testPC3))
                .build();

        menuItemDTO = MenuItemDTO.builder()
                .stock(10)
                .build();

        testPC.setPricingList(Arrays.asList(testPaket, testTarif));
        testPC2.setPricingList(Arrays.asList(testPaket, testTarif));
        testPC3.setPricingList(Arrays.asList(testPaket, testTarif));

        pricingByPCResponsePaket = new PricingByPCResponse(1, "Paket 1", 1, 1, testPaket.getMakananId());
        pricingByPCResponseTarif = new PricingByPCResponse(2, "Tarif 1", 1, 1, null);

        session = Session.builder()
                .pc(testPC)
                .datetimeStart(initialTime)
                .datetimeEnd(initialTime.plusHours(duration))
                .build();
        session.setId(UUID.randomUUID());

        endSessionSuccess = Session.builder()
                .pc(testPC2)
                .datetimeStart(initialTime)
                .datetimeEnd(initialTime.plusHours(3))
                .build();
        endSessionSuccess.setId(UUID.randomUUID());

        endSessionExpired = Session.builder()
                .pc(testPC3)
                .datetimeStart(initialTime.minusHours(2))
                .datetimeEnd(initialTime.minusHours(1))
                .build();
        endSessionExpired.setId(UUID.randomUUID());

        createRequest = CreateSessionRequest.builder()
                .pcId(testPC.getId())
                .pricingId(testTarif.getId())
                .quantity(2)
                .build();

        validCreateResponse = CreateSessionResponse.builder()
                .id(validId)
                .pcId(testPC.getId())
                .noPC(testPC.getNoPC())
                .noRuangan(testPC.getNoRuangan())
                .pricingId(testTarif.getId())
                .namaPricing(testTarif.getName())
                .totalHarga(testTarif.getPrice() * 2)
                .totalDurasi(testTarif.getDuration() * 2)
                .build();
    }
    
    @Test
    void testCreateSessionSuccessTarif() {
        when(pcExistenceValidationHandler.setNextHandler(pricingExistenceValidationHandler)).thenReturn(pricingExistenceValidationHandler);
        when(pricingExistenceValidationHandler.setNextHandler(pcPricingPairValidationHandler)).thenReturn(pcPricingPairValidationHandler);
        when(pcPricingPairValidationHandler.setNextHandler(pcUsageValidationHandler)).thenReturn(pcUsageValidationHandler);
        when(pcUsageValidationHandler.setNextHandler(quantityValidationHandler)).thenReturn(quantityValidationHandler);
        when(quantityValidationHandler.setNextHandler(foodAvailabilityValidationHandler)).thenReturn(foodAvailabilityValidationHandler);
        doNothing().when(pcExistenceValidationHandler).handleRequest(any(CreateSessionRequest.class));

        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            Session session1 = invocation.getArgument(0, Session.class);
            session1.setId(validCreateResponse.getId());
            return session1;
        });

        when(sessionPricingRepository.save(any(SessionPricing.class))).thenAnswer(invocation ->
            invocation.getArgument(0, SessionPricing.class)
        );
        when(pcRepository.findById(testPC.getId())).thenReturn(Optional.of(testPC));
        when(pricingRepository.findById(testTarif.getId())).thenReturn(Optional.of(testTarif));

        when(bayarService.addToBillFromMicroservice(any(BillRequest.class))).thenReturn("SUCCESS");

        CreateSessionResponse response = service.createSession(createRequest);
        verify(sessionRepository, atLeastOnce()).save(any(Session.class));
        verify(sessionPricingRepository, atLeastOnce()).save(any(SessionPricing.class));

        assertEquals(response, validCreateResponse);
    }

    @Test
    void testCreateSessionSuccessPaket() {
        when(pcExistenceValidationHandler.setNextHandler(pricingExistenceValidationHandler)).thenReturn(pricingExistenceValidationHandler);
        when(pricingExistenceValidationHandler.setNextHandler(pcPricingPairValidationHandler)).thenReturn(pcPricingPairValidationHandler);
        when(pcPricingPairValidationHandler.setNextHandler(pcUsageValidationHandler)).thenReturn(pcUsageValidationHandler);
        when(pcUsageValidationHandler.setNextHandler(quantityValidationHandler)).thenReturn(quantityValidationHandler);
        when(quantityValidationHandler.setNextHandler(foodAvailabilityValidationHandler)).thenReturn(foodAvailabilityValidationHandler);
        doNothing().when(pcExistenceValidationHandler).handleRequest(any(CreateSessionRequest.class));

        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> {
            Session session1 = invocation.getArgument(0, Session.class);
            session1.setId(validCreateResponse.getId());
            return session1;
        });

        when(sessionPricingRepository.save(any(SessionPricing.class))).thenAnswer(invocation ->
            invocation.getArgument(0, SessionPricing.class)
        );

        when(pcRepository.findById(testPC.getId())).thenReturn(Optional.of(testPC));
        when(pricingRepository.findById(testPaket.getId())).thenReturn(Optional.of(testPaket));

        when(bayarService.addToBillFromMicroservice(any(BillRequest.class))).thenReturn("SUCCESS");

        createRequest.setPricingId(testPaket.getId());
        CreateSessionResponse response = service.createSession(createRequest);
        verify(sessionRepository, atLeastOnce()).save(any(Session.class));
        verify(sessionPricingRepository, atLeastOnce()).save(any(SessionPricing.class));

        validCreateResponse.setPricingId(testPaket.getId());
        validCreateResponse.setNamaPricing(testPaket.getName());
        assertEquals(response, validCreateResponse);
    }

    @Test
    void testCreateSessionTarifSuccessSessionListNotEmpty () {
        when(pcRepository.findById(testPC.getId())).thenReturn(Optional.of(testPC));

        testPC.setSessionList(List.of(Session.builder().build()));
        testCreateSessionSuccessTarif();
    }

    @Test
    void testGetSessionSuccess(){
        when(sessionRepository.findById(session.getId())).thenReturn(Optional.of(session));
        
        Session retrievedSession = service.getSession(session.getId());

        assertEquals(session.getId(), retrievedSession.getId());
        verify(sessionRepository, atLeastOnce()).findById(session.getId());
    }

    @Test
    void testGetSessionFailIdDoesNotExist(){
        UUID sessionUuid = session.getId();
        when(sessionRepository.findById(sessionUuid)).thenReturn(Optional.empty());
        assertThrows(SessionDoesNotExistException.class, () -> service.getSession(sessionUuid));
        verify(sessionRepository, atLeastOnce()).findById(sessionUuid);
    }

    @Test
    void testGetAllPricingPCDoesNotExist() {
        when(kelolaWarnetService.getPCById(anyInt())).thenThrow(PCDoesNotExistException.class);
        assertThrows(PCDoesNotExistException.class, () -> service.getAllPricingbyPC(-1, true));
    }

    @Test
    void testGetAllPaketByPCShouldReturnPakets() {
        List<PricingByPCResponse> expectedAllPaket = List.of(pricingByPCResponsePaket);

        when(kelolaWarnetService.getPCById(anyInt())).thenReturn(testPC);
        when(cafeService.getMenuItemByIdFromMicroservice(testPaket.getMakananId())).thenReturn(menuItemDTO);

        List<PricingByPCResponse> result = service.getAllPricingbyPC(testPC.getId(), true);

        Assertions.assertEquals(expectedAllPaket, result);
    }

    @Test
    void testGetAllPaketOutOfStockByPCShouldReturnEmptyList() {
        when(kelolaWarnetService.getPCById(anyInt())).thenReturn(testPC);
        menuItemDTO.setStock(0);
        when(cafeService.getMenuItemByIdFromMicroservice(testPaket.getMakananId())).thenReturn(menuItemDTO);

        List<PricingByPCResponse> result = service.getAllPricingbyPC(testPC.getId(), true);

        Assertions.assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testGetAllTarifByPCShouldReturnTarifs() {
        List<PricingByPCResponse> allTarif = List.of(pricingByPCResponseTarif);

        when(kelolaWarnetService.getPCById(anyInt())).thenReturn(testPC);
        List<PricingByPCResponse> result = service.getAllPricingbyPC(testPC.getId(), false);

        Assertions.assertEquals(allTarif, result);
    }

    @Test
    void testGetAllPaketByPCInterruptedShouldThrowException() {

        when(kelolaWarnetService.getPCById(anyInt())).thenReturn(testPC);

        when(cafeService.getMenuItemByIdFromMicroservice(testPaket.getMakananId())).thenThrow(new RuntimeException(new InterruptedException()));

        Integer pcId = testPC.getId();
        Assertions.assertThrows(PCInterruptedException.class, () -> {
            service.getAllPricingbyPC(pcId, true);
        });
    }

    @Test
    void testEndSessionSuccess() {
        Session oldEndSessionSuccess = Session.builder()
                                            .id(endSessionSuccess.getId())
                                            .pc(endSessionSuccess.getPc())
                                            .datetimeStart(endSessionSuccess.getDatetimeStart())
                                            .datetimeEnd(endSessionSuccess.getDatetimeEnd())
                                            .build();
        when(sessionRepository.findById(endSessionSuccess.getId())).thenReturn(Optional.of(endSessionSuccess));
        when(sessionRepository.save(any(Session.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Session result = service.endSession(endSessionSuccess.getId());
        verify(sessionRepository, atLeastOnce()).save(any(Session.class));

        assertEquals(oldEndSessionSuccess.getDatetimeStart(), result.getDatetimeStart());
        assertTrue(result.getDatetimeEnd().isBefore(oldEndSessionSuccess.getDatetimeEnd()));
    }


    @Test
    void testEndSessionExpiredThrowsError() {
        UUID endSessionExpiredId = endSessionExpired.getId();
        when(sessionRepository.findById(endSessionExpiredId)).thenReturn(Optional.of(endSessionExpired));

        assertThrows(SessionExpiredException.class, () -> 
            service.endSession(endSessionExpiredId)
        );
        verify(sessionRepository, times(1)).findById(endSessionExpiredId);
        verify(sessionRepository, never()).save(any());
    }
}
