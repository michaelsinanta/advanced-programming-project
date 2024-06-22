package id.ac.ui.cs.advprog.warnetservice.service;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.GetAllSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionDetailResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionPricingResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionDoesNotExistException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.service.sessioninfo.SessionInfoServiceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SessionInfoServiceImplTest {

    @InjectMocks
    private SessionInfoServiceImpl service;

    @Mock
    private SessionRepository repository;

    @Mock
    private SessionPricingRepository sessionPricingRepository;

    Session session;
    PC testPC;
    LocalDateTime initialTime;
    Long duration;
    Pricing pricing;
    SessionPricing sessionPricing;
    SessionPricingResponse sessionPricingResponse;
    SessionDetailResponse sessionDetailResponse;
    GetAllSessionResponse getAllSessionResponse;

    @BeforeEach
    void setUp() {
        initialTime = LocalDateTime.now();
        duration = 2L;

        testPC = PC.builder()
                .noPC(1)
                .noRuangan(1)
                .build();

        testPC.setId(1);

        session = Session.builder()
                .id(UUID.randomUUID())
                .pc(testPC)
                .datetimeStart(initialTime)
                .datetimeEnd(initialTime.plusHours(duration))
                .build();
        
        pricing = Pricing.builder()
                    .id(1).name("Paket Tengah Malam")
                    .isPaket(true).duration(3)
                    .price(25000)
                    .makananId("49a7ac1b-f700-40c1-8454-9357bdbf20e2")
                    .build();

        sessionPricing = SessionPricing.builder()
                            .session(session)
                            .pricing(pricing)
                            .waktuPembelian(initialTime)
                            .quantity(1)
                            .build();
        
        sessionPricingResponse = new SessionPricingResponse(pricing, initialTime, 1);

        sessionDetailResponse = SessionDetailResponse.builder()
                                    .session(session)
                                    .sessionPricingList(List.of(sessionPricingResponse))
                                    .build();

        getAllSessionResponse = GetAllSessionResponse.builder()
                            .noRuangan(session.getPc().getNoRuangan())
                            .pcId(session.getPc().getId())
                            .noPC(session.getPc().getNoPC())
                            .datetimeStart(session.getDatetimeStart())
                            .datetimeEnd(session.getDatetimeEnd())
                            .id(session.getId())
                            .build();
    }

    @Test
    void testGetAllSessionNotFilteredSuccess(){
        List<Session> resp = List.of(session);
        List<GetAllSessionResponse> expected = List.of(getAllSessionResponse);

        when(repository.listSession(0,1,null,null)).thenReturn(resp);
        List<GetAllSessionResponse> result = service.getAllSession(1,1,null,null);

        verify(repository,atLeastOnce()).listSession(0,1,null,null);
        assertEquals(expected,result);
    }

    @Test
    void testGetAllSessionFilteredByPcIdSuccess(){
        List<Session> resp = List.of(session);
        List<GetAllSessionResponse> expected = List.of(getAllSessionResponse);

        when(repository.listSession(0,1,session.getPc().getId(),null)).thenReturn(resp);
        List<GetAllSessionResponse> result = service.getAllSession(1,1,session.getPc().getId(),null);

        verify(repository,atLeastOnce()).listSession(0,1,session.getPc().getId(),null);
        assertEquals(expected,result);
    }

    @Test
    void testGetAllSessionFilteredByDateSuccess(){
        List<Session> resp = List.of(session);
        List<GetAllSessionResponse> expected = List.of(getAllSessionResponse);
        LocalDate date = LocalDate.now();

        when(repository.listSession(0,1,null,date.toString())).thenReturn(resp);
        List<GetAllSessionResponse> result = service.getAllSession(1,1,null,date.toString());

        verify(repository,atLeastOnce()).listSession(0,1,null,date.toString());
        assertEquals(expected,result);
    }

    @Test
    void testGetAllSessionFilteredByDateShouldReturnEmpty(){
        List<Session> resp = List.of(session);
        List<GetAllSessionResponse> expected = Collections.emptyList();
        LocalDate date = LocalDate.now();
        date = date.minusDays(1);

        when(repository.listSession(0,1,null,date.toString())).thenReturn(Collections.emptyList());
        List<GetAllSessionResponse> result = service.getAllSession(1,1,null,date.toString());

        verify(repository,atLeastOnce()).listSession(0,1,null,date.toString());
        assertEquals(expected,result);
    }

    @Test
    void testGetAllSessionFilteredByPcIdShouldReturnEmpty(){
        List<Session> resp = List.of(session);
        List<GetAllSessionResponse> expected = Collections.emptyList();

        when(repository.listSession(0,1,session.getPc().getId() + 200,null)).thenReturn(Collections.emptyList());
        List<GetAllSessionResponse> result = service.getAllSession(1,1,session.getPc().getId() + 200,null);

        verify(repository,atLeastOnce()).listSession(0,1,session.getPc().getId() + 200,null);
        assertEquals(expected,result);
    }

    @Test
    void testGetSessionDetailSuccess() {

        when(repository.findById(session.getId())).thenReturn(Optional.of(session));
        when(sessionPricingRepository.findBySession(session)).thenReturn(List.of(sessionPricing));

        SessionDetailResponse response = service.getSessionDetail(session.getId());
        verify(repository, atLeastOnce()).findById(session.getId());
        verify(sessionPricingRepository, atLeastOnce()).findBySession(session);
        Assertions.assertEquals(sessionDetailResponse, response);
    }

    @Test
    void testGetSessionDetailFailedSessionNotFound() {

        UUID sessionId = session.getId();
        when(repository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(SessionDoesNotExistException.class, () -> service.getSessionDetail(sessionId));

        verify(repository, atLeastOnce()).findById(session.getId());
    }
}
