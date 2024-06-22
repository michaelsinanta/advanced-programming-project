package id.ac.ui.cs.advprog.warnetservice.controller;

import id.ac.ui.cs.advprog.warnetservice.Util;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.ExtendSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.ExtendSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.*;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.service.perpanjang.PerpanjangServiceImpl;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PerpanjangController.class)
@AutoConfigureMockMvc
class PerpanjangControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PerpanjangServiceImpl service;

    Object bodyContent;
    UUID sessionUUID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        bodyContent = new Object() {
            public final UUID id = sessionUUID;
            public final Integer pricingId = 1;
            public final Integer quantity = 1;
        };
    }

    @Test
    void testExtendSessionShouldSucceed() throws Exception {
        Session response = Session.builder().id(sessionUUID).build();

        when(service.extendSession(any(ExtendSessionRequest.class))).thenReturn(response);

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("extendSession"))
                .andExpect(jsonPath("$.id").value(sessionUUID.toString()));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendSessionIsExpiredShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new SessionExpiredException(sessionUUID.toString()));

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isForbidden())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendSessionNotFoundShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new SessionDoesNotExistException(sessionUUID.toString()));

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendPricingNotFoundShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new PricingDoesNotExistException(any(Integer.class)));

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendQuantityInvalidShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new NonPositiveParameterException("quantity"));

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendPricingInvalidShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new PCPricingPairDoesNotExistException(null, null));

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }

    @Test
    void testExtendFoodSoldOutShouldFail() throws Exception {
        when(service.extendSession(any(ExtendSessionRequest.class)))
                .thenThrow(new FoodSoldOutException());

        mvc.perform(post("/warnet/perpanjang/tambah_pricing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(bodyContent)))
                .andExpect(status().isConflict())
                .andExpect(handler().methodName("extendSession"));

        verify(service, atLeastOnce()).extendSession(any(ExtendSessionRequest.class));
    }
}
