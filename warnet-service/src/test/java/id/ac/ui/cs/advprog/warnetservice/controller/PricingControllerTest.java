package id.ac.ui.cs.advprog.warnetservice.controller;

import id.ac.ui.cs.advprog.warnetservice.Util;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.PricingByPCResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCPricingPairDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.SessionExpiredException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.PricingServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PricingController.class)
@AutoConfigureMockMvc
class PricingControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PricingServiceImpl service;

    PricingByPCResponse pricingByPCResponseTarif;
    PricingByPCResponse pricingByPCResponsePaket;
    Object bodyContent;
    Session session;
    PC testPC;
    LocalDateTime initialTime;
    Long duration;
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

        pricingByPCResponsePaket = new PricingByPCResponse(1, "Paket 1", 1, 1, "");
        pricingByPCResponseTarif = new PricingByPCResponse(2, "Tarif 1", 1, 1, "");

        bodyContent = new Object() {
            public final PC pc = testPC;
            public final LocalDateTime datetimeStart = initialTime;
            public final LocalDateTime datetimeEnd = initialTime.plusHours(2);
        };
        session.setId(UUID.randomUUID());

    }

    @Test
    void testGetSession() throws  Exception{
        when(service.getSession(any(UUID.class))).thenReturn(session);

        mvc.perform(get(("/warnet/sewa_pc/get_session/" + session.getId().toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getSession"))
                .andExpect(jsonPath("$.pc.noPC").value(session.getPc().getNoRuangan()));

        verify(service, atLeastOnce()).getSession(any(UUID.class));
    }

    @Test
    void testGetSessionFailedShouldReturnNotFound() throws Exception{
        UUID uuid = UUID.randomUUID();
        when(service.getSession(uuid)).thenThrow(new SessionDoesNotExistException(uuid.toString()));

        mvc.perform(get(String.format("/warnet/sewa_pc/get_session/%s", uuid.toString()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getSession"));

        verify(service, atLeastOnce()).getSession(uuid);
    }
    
    @Test
    void testCreateSession() throws Exception {
        CreateSessionResponse createSessionResponse = new CreateSessionResponse();
        when(service.createSession(any(CreateSessionRequest.class))).thenReturn(createSessionResponse);

        mvc.perform(post("/warnet/sewa_pc/create_session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createSession"))
                .andExpect(jsonPath("$.noPC").value(createSessionResponse.getNoPC()));

        verify(service, atLeastOnce()).createSession(any(CreateSessionRequest.class));
    }

    @Test
    void testGetAllPaketbyPC() throws Exception {
        List<PricingByPCResponse> allPaket = List.of(pricingByPCResponsePaket);

        when(service.getAllPricingbyPC(anyInt(), anyBoolean())).thenReturn(allPaket);

        mvc.perform(get("/warnet/sewa_pc/get_pricing_by_pc/1/true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPricingByPC"))
                .andExpect(jsonPath("$[0].pricingId").value(pricingByPCResponsePaket.getPricingId()));

        verify(service, atLeastOnce()).getAllPricingbyPC(testPC.getId(), true);
    }

    @Test
    void testGetAllTarifbyPC() throws Exception {
        List<PricingByPCResponse> allTarif = List.of(pricingByPCResponseTarif);

        when(service.getAllPricingbyPC(anyInt(), anyBoolean())).thenReturn(allTarif);

        mvc.perform(get("/warnet/sewa_pc/get_pricing_by_pc/1/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPricingByPC"))
                .andExpect(jsonPath("$[0].pricingId").value(pricingByPCResponseTarif.getPricingId()));

        verify(service, atLeastOnce()).getAllPricingbyPC(testPC.getId(), false);
    }

    @Test
    void testGetAllPricingByPCFailedShouldReturnNotFound() throws Exception{
        when(service.getAllPricingbyPC(100, false)).thenThrow(new PCDoesNotExistException(100));

        mvc.perform(get("/warnet/sewa_pc/get_pricing_by_pc/100/false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getAllPricingByPC"));

        verify(service, atLeastOnce()).getAllPricingbyPC(100, false);
    }

    @Test
    void testEndSession() throws Exception {
        when(service.endSession(any(UUID.class))).thenReturn(session);
        String uuidStr = session.getId().toString();

        mvc.perform(post("/warnet/sewa_pc/session_done/" + uuidStr)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("sessionDone"))
                .andExpect(jsonPath("$.id").value(uuidStr));

        verify(service, atLeastOnce()).endSession(any(UUID.class));
    }

    @Test
    void testEndSessionFailedSessionDoesNotFoundShouldReturnNotFound() throws Exception{
        UUID uuid = UUID.randomUUID();
        when(service.endSession(any(UUID.class))).thenThrow(new SessionDoesNotExistException(uuid.toString()));

        mvc.perform(post("/warnet/sewa_pc/session_done/" +uuid.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("sessionDone"));

        verify(service, atLeastOnce()).endSession(uuid);
    }

    @Test
    void testEndSessionFailedSessionAlreadyDoneShouldReturnForbidden() throws Exception{
        session.setDatetimeEnd(LocalDateTime.now().minusMinutes(1));
        when(service.endSession(any(UUID.class))).thenThrow(new SessionExpiredException(session.getId().toString()));

        mvc.perform(post("/warnet/sewa_pc/session_done/" + session.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(handler().methodName("sessionDone"));

        verify(service, atLeastOnce()).endSession(session.getId());
    }

}

