package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.invoice.InvoiceRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.service.invoice.InvoiceServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InvoiceController.class)
@AutoConfigureMockMvc
class InvoiceControllerTest {
    private static final String END_POINT_PATH = "/api/v1/invoices";

    @Autowired
    private MockMvc mockMvc;  // to mock http request

    @MockBean
    private InvoiceServiceImpl invoiceService;

    @Test
    void testAddInvoiceShouldReturn201Created() throws Exception {
        Invoice invoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        when(invoiceService.create(any(InvoiceRequest.class))).thenReturn(invoice);

        String requestBody = Util.mapToJson(invoice);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(handler().methodName("addInvoice"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.sessionId").value(invoice.getSessionId().toString()))
                .andDo(print());

        verify(invoiceService, atLeastOnce()).create(any(InvoiceRequest.class));
    }

    @Test
    void testAddInvoiceShouldReturn400BadRequest() throws Exception {
        Invoice invoice = Invoice.builder().build();

        String requestBody = Util.mapToJson(invoice);

        mockMvc.perform(post(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("addInvoice"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testAddInvoiceShouldReturn405MethodNotAllowed() throws Exception {
        Invoice invoice = Invoice.builder().build();

        String requestBody = Util.mapToJson(invoice);

        mockMvc.perform(get(END_POINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testGetInvoiceByIdShouldReturn200OK() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/id/" + invoiceId;

        Invoice invoice = Invoice.builder()
                .id(invoiceId)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        when(invoiceService.findById(invoiceId)).thenReturn(invoice);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getInvoiceById"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.id").value(invoice.getId()))
                .andDo(print());

        verify(invoiceService, atLeastOnce()).findById(invoiceId);
    }

    @Test
    void testGetInvoiceByIdShouldReturn404NotFound() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/id/" + invoiceId;

        when(invoiceService.findById(invoiceId))
                .thenThrow(InvoiceDoesNotExistException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getInvoiceById"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        verify(invoiceService, atMostOnce()).findById(invoiceId);
    }

    @Test
    void testGetInvoiceByIdShouldReturn405MethodNotAllowed() throws Exception {
        int invoiceId = 123;
        String requestURI = END_POINT_PATH + "/id/" + invoiceId;

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testGetInvoiceBySessionIdShouldReturn200OK() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "/" + sessionId;

        Invoice invoice = Invoice.builder()
                .sessionId(sessionId)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();

        when(invoiceService.findBySessionId(sessionId)).thenReturn(invoice);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getInvoiceBySessionId"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.id").value(invoice.getId()))
                .andExpect(jsonPath("$.content.sessionId").value(invoice.getSessionId().toString()))
                .andDo(print());

        verify(invoiceService, atLeastOnce()).findBySessionId(sessionId);
    }

    @Test
    void testGetInvoiceBySessionIdShouldReturn404NotFound() throws Exception {
        UUID sessionId = UUID.randomUUID();
        String requestURI = END_POINT_PATH + "/" + sessionId;

        when(invoiceService.findBySessionId(sessionId))
                .thenThrow(InvoiceDoesNotExistException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getInvoiceBySessionId"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        verify(invoiceService, atMostOnce()).findBySessionId(sessionId);
    }
}
