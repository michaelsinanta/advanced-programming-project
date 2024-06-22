package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.service.discount.DiscountServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.model.discount.DiscountType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = DiscountController.class)
@AutoConfigureMockMvc
class DiscountControllerTest {
    private static final String END_POINT_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc; // to mock http request

    DiscountRequest nominalDiscountRequest;
    DiscountRequest percentageDiscountRequest;
    @MockBean
    DiscountServiceImpl discountService;

    @BeforeEach
    void setUp() {
        nominalDiscountRequest = DiscountRequest.builder()
                .discountType(String.valueOf(DiscountType.NOMINAL))
                .discount(50000L)
                .build();

        percentageDiscountRequest = DiscountRequest.builder()
                .discountType(String.valueOf(DiscountType.PERCENTAGE))
                .discount(50L)
                .build();
    }

    @Test
    void testGiveNominalDiscount() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "sessions/" + sessionId + "/discount";

        String requestBody = Util.mapToJson(nominalDiscountRequest);

        mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("giveDiscount"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    void testGivePercentageDiscount() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "sessions/" + sessionId + "/discount";

        String requestBody = Util.mapToJson(percentageDiscountRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("giveDiscount"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }
}
