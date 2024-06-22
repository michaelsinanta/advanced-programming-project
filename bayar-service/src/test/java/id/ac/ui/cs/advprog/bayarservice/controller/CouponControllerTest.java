package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponServiceImpl;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CouponController.class)
@AutoConfigureMockMvc
class CouponControllerTest {
    private static final String END_POINT_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc; // to mock http request

    @MockBean
    private CouponServiceImpl couponService;

    Coupon coupon;

    CouponRequest couponRequest;

    UseCouponRequest useCouponRequest;

    @BeforeEach
    void setUp() {
        coupon = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

        couponRequest = CouponRequest.builder()
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

        useCouponRequest = UseCouponRequest.builder()
                .name("SEPTEMBERCERIA")
                .build();
    }

    @Test
    void testUpdateCoupon() throws Exception {
        int couponId = 123;
        String requestURI = END_POINT_PATH + "coupons/" + couponId;

        when(couponService.update(any(Integer.class), any(CouponRequest.class))).thenReturn(coupon);

        String requestBody = Util.mapToJson(couponRequest);

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.name").value(coupon.getName()))
                .andDo(print());

        verify(couponService, atLeastOnce()).update(any(Integer.class), any(CouponRequest.class));
    }

    @Test
    void testUseCoupon() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "sessions/" + sessionId + "/coupons/use";

        String requestBody = Util.mapToJson(useCouponRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("useCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());
    }

    @Test
    void testCreateCouponShouldReturn200OK() throws Exception {
        String requestURI = END_POINT_PATH + "coupons/createCoupon";

        when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(coupon);

        String requestBody = Util.mapToJson(couponRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.name").value(coupon.getName()))
                .andDo(print());

        verify(couponService, atLeastOnce()).createCoupon(any(CouponRequest.class));
    }

    @Test
    void testDeleteCouponShouldReturn200OK() throws Exception {
        int couponId = 123;
        String requestURI = END_POINT_PATH + "coupons/delete/" + couponId;

        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

       String requestBody = Util.mapToJson(coupon);

       when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(coupon);

        mockMvc.perform(post(END_POINT_PATH + "coupons/createCoupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        mockMvc.perform(delete(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andDo(print());

        verify(couponService, atLeastOnce()).deleteCoupon(any(Integer.class));
    }

    @Test
    void testCreateCouponShouldReturn405NotAllowed() throws Exception {
        String requestURI = END_POINT_PATH + "coupons/createCoupon";

        when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(coupon);

        String requestBody = Util.mapToJson(couponRequest);

        mockMvc.perform(get(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

        mockMvc.perform(delete(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    void testDeleteCouponShouldReturn405NotAllowed() throws Exception {
        int couponId = 123;
        String requestURI = END_POINT_PATH + "coupons/delete/" + couponId;

        Coupon coupon = Coupon.builder()
                .id(couponId)
                .name("SEPTEMBERCERIA")
                .discount(50000L)
                .build();

       String requestBody = Util.mapToJson(coupon);

        mockMvc.perform(get(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    void testCreateCouponShouldReturn400BadRequest() throws Exception {
        String requestURI = END_POINT_PATH + "coupons/createCoupon";

        Coupon emptyCouponName = Coupon.builder()
                .name("")
                .discount(50000L)
                .build();

        Coupon emptyCouponDiscount = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(null)
                .build();

        Coupon negativeCouponDiscount = Coupon.builder()
                .name("SEPTEMBERCERIA")
                .discount(-50000L)
                .build();

        Coupon emptyRequest = Coupon.builder()
                .build();

        String requestBodyEmptyCouponName = Util.mapToJson(emptyCouponName);

        String requestBodyEmptyCouponDiscount = Util.mapToJson(emptyCouponDiscount);

        String requestBodyNegativeCouponDiscount = Util.mapToJson(negativeCouponDiscount);

        String requestBodyFullyEmpty = Util.mapToJson(emptyRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptyCouponName))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptyCouponDiscount))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyFullyEmpty))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyNegativeCouponDiscount))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testCreateCouponShouldReturn409Conflict() throws Exception {
        String requestURI = END_POINT_PATH + "coupons/createCoupon";

        when(couponService.createCoupon(any(CouponRequest.class))).thenReturn(coupon);

        String requestBody = Util.mapToJson(couponRequest);

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("createCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.name").value(coupon.getName()))
                .andDo(print());

        when(couponService.createCoupon(any(CouponRequest.class))).thenThrow(new CouponAlreadyExistException(couponRequest.getName()));

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(couponService, atLeastOnce()).createCoupon(any(CouponRequest.class));
    }

    @Test
    void testDeleteCouponShouldReturn404NotFound() throws Exception {
        int couponId = 123;
        String requestURI = END_POINT_PATH + "coupons/delete/" + couponId;
        Mockito.doThrow(new CouponDoesNotExistException(couponId)).when(couponService).deleteCoupon(any(Integer.class));

        mockMvc.perform(delete(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(couponService, atLeastOnce()).deleteCoupon(any(Integer.class));
    }

    @Test
    void testGetAllCouponShouldReturn200OK() throws Exception {
        String requestURI = END_POINT_PATH + "coupons/getAll";

        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon);

        when(couponService.getAllCoupon()).thenReturn(couponList);

        mockMvc.perform(get(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllCoupon"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content[0].name").value(coupon.getName()))
                .andDo(print());

        verify(couponService, atLeastOnce()).getAllCoupon();
    }
}
