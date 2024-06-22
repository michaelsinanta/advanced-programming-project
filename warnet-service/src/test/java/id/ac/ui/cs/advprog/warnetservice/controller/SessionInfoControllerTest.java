package id.ac.ui.cs.advprog.warnetservice.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.warnetservice.Util;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.GetAllSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionDetailResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionPricingResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Pricing;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import id.ac.ui.cs.advprog.warnetservice.service.sessioninfo.SessionInfoServiceImpl;

@WebMvcTest(controllers = SessionInfoController.class)
@AutoConfigureMockMvc
class SessionInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SessionInfoServiceImpl service;

    Session session;
    PC testPC;
    SessionDetailResponse sessionDetailResponse;
    LocalDateTime initialTime;
    Long duration;
    SessionPricingResponse sessionPricingResponse;
    Pricing pricing;
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
    void testGetAllSessionNotFilteredSuccess() throws Exception {
        List<GetAllSessionResponse> response = new ArrayList<>();
        response.add(getAllSessionResponse);
        when(service.getAllSession(1,3,null,null)).thenReturn(response);

        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSession"))
                .andExpect(jsonPath("$..pcId").value(session.getPc().getId()))
                .andExpect(jsonPath("$..noPC").value(session.getPc().getNoPC()))
                .andExpect(jsonPath("$..noRuangan").value(session.getPc().getNoRuangan()))
                .andExpect(jsonPath("$..id").value(session.getId().toString()));
        verify(service, atLeastOnce()).getAllSession(1,3,null,null);
    }

    @Test
    void testGetAllSessionFilteredByIdSuccess() throws Exception{
        List<GetAllSessionResponse> response = new ArrayList<>();
        response.add(getAllSessionResponse);
        when(service.getAllSession(1,1,1,null)).thenReturn(response);

        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=1&pcId=1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSession"))
                .andExpect(jsonPath("$..pcId").value(session.getPc().getId()))
                .andExpect(jsonPath("$..noRuangan").value(session.getPc().getId()))
                .andExpect(jsonPath("$..noPC").value(session.getPc().getNoPC()))
                .andExpect(jsonPath("$..id").value(session.getId().toString()));

        verify(service,atLeastOnce()).getAllSession(1,1,1,null);
    }

    @Test
    void testGetAllSessionFilteredByDateSuccess() throws Exception{
        List<GetAllSessionResponse> response = new ArrayList<>();
        response.add(getAllSessionResponse);
        LocalDate date = LocalDate.now();
        when(service.getAllSession(1,1,null,date.toString())).thenReturn(response);
        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=1&date=" + date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSession"))
                .andExpect(jsonPath("$..pcId").value(session.getPc().getId()))
                .andExpect(jsonPath("$..noRuangan").value(session.getPc().getId()))
                .andExpect(jsonPath("$..noPC").value(session.getPc().getNoPC()))
                .andExpect(jsonPath("$..id").value(session.getId().toString()));

        verify(service,atLeastOnce()).getAllSession(1,1,null,date.toString());
    }

    @Test
    void testGetAllSessionFilteredByDateShouldReturnEmpty() throws Exception{
        LocalDate date = LocalDate.now();
        date = date.minusDays(1);

        when(service.getAllSession(1,1,null,date.toString())).thenReturn(Collections.emptyList());
        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=1&date=" + date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSession"))
                .andExpect(jsonPath("$").isEmpty());

        verify(service,atLeastOnce()).getAllSession(1,1,null,date.toString());
    }

    @Test
    void testGetAllSessionFilteredByInvalidDateShouldReturnBadRequest() throws Exception{
        LocalDate date = LocalDate.now();
        date = date.minusDays(1);

        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=1&date=28-09-1999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("getAllSession"));
    }

    @Test
    void testGetAllSessionFilteredByPcIdShouldReturnEmpty() throws Exception{
        when(service.getAllSession(1,1,101,null)).thenReturn(Collections.emptyList());

        mvc.perform(get("/warnet/info_sesi/all_session?page=1&limit=1&pcId=101")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllSession"))
                .andExpect(jsonPath("$").isEmpty());

        verify(service,atLeastOnce()).getAllSession(1,1,101,null);
    }

    @Test
    void testGetSessionDetailSuccess() throws Exception {
        when(service.getSessionDetail(session.getId())).thenReturn(sessionDetailResponse);

        mvc.perform(get("/warnet/info_sesi/session_detail/" + session.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("getSessionById"))
            .andExpect(jsonPath("$.session.id").value(session.getId().toString()))
            .andExpect(jsonPath("$.session.pc.id").value(session.getPc().getId()))
            .andExpect(jsonPath("$.session.pc.noPC").value(session.getPc().getNoPC()))
            .andExpect(jsonPath("$.session.pc.noRuangan").value(session.getPc().getNoRuangan()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.id").value(sessionPricingResponse.getPricing().getId()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.name").value(sessionPricingResponse.getPricing().getName()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.price").value(sessionPricingResponse.getPricing().getPrice()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.duration").value(sessionPricingResponse.getPricing().getDuration()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.isPaket").value(sessionPricingResponse.getPricing().getIsPaket()))
            .andExpect(jsonPath("$.sessionPricingList[0].pricing.makananId").value(sessionPricingResponse.getPricing().getMakananId()))
            .andExpect(jsonPath("$.sessionPricingList[0].quantity").value(sessionPricingResponse.getQuantity()));


        verify(service, atLeastOnce()).getSessionDetail(session.getId());
    }

    @Test
    void testGetSessionDetailWithNoPricingShouldHaveEmptySessionPricingList() throws Exception {
        sessionDetailResponse.setSessionPricingList(Collections.emptyList());
        when(service.getSessionDetail(session.getId())).thenReturn(sessionDetailResponse);

        mvc.perform(get("/warnet/info_sesi/session_detail/" + session.getId().toString())
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(handler().methodName("getSessionById"))
            .andExpect(jsonPath("$.session.id").value(session.getId().toString()))
            .andExpect(jsonPath("$.session.pc.id").value(session.getPc().getId()))
            .andExpect(jsonPath("$.session.pc.noPC").value(session.getPc().getNoPC()))
            .andExpect(jsonPath("$.session.pc.noRuangan").value(session.getPc().getNoRuangan()))
            .andExpect(jsonPath("$.sessionPricingList").isEmpty());


        verify(service, atLeastOnce()).getSessionDetail(session.getId());
    }

    @Test
    void testGetSessionDetailFailedShouldReturnNotFound() throws Exception {
        when(service.getSessionDetail(session.getId())).thenThrow(new SessionDoesNotExistException(session.getId().toString()));

        mvc.perform(get("/warnet/info_sesi/session_detail/" + session.getId().toString())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getSessionById"));

        verify(service,atLeastOnce()).getSessionDetail(session.getId());
    }
}
