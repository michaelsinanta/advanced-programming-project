package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.BillDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.PaymentMethod;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.model.invoice.Invoice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import id.ac.ui.cs.advprog.bayarservice.repository.InvoiceRepository;


import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BillController.class)
@AutoConfigureMockMvc
class BillControllerTest {
    private static final String END_POINT_PATH = "/api/v1/bills";

    @Autowired
    private MockMvc mockMvc; // to mock http request

    @MockBean
    private BillServiceImpl billService;

    @MockBean
    private InvoiceRepository invoiceRepository;

    Bill bill;

    BillRequest billRequest;

    @BeforeEach
    void setUp() {
        bill = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        billRequest = BillRequest.builder()
                .name("Coffee Updated")
                .quantity(10)
                .price(10000)
                .subTotal(100000L)
                .build();
    }

    @Test
    void testGetBillByIdShouldReturn200OK() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.findById(billId)).thenReturn(bill);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getBillById"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content.id").value(bill.getId()))
                .andDo(print());

        verify(billService, atLeastOnce()).findById(billId);
    }

    @Test
    void testGetBillByIdShouldReturn404NotFound() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        when(billService.findById(billId)).thenThrow(BillDoesNotExistException.class);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getBillById"))
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        verify(billService, atMostOnce()).findById(billId);
    }

    @Test
    void testGetBillByIdShouldReturn405MethodNotAllowed() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/" + billId;

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testDeleteBillByIdShouldReturn200OK () throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/delete/" + billId;

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.findById(billId)).thenReturn(bill);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteBillById"))
                .andDo(print());

        verify(billService, atLeastOnce()).delete(billId);
    }

    @Test
    void testDeleteBillByIdShouldReturn405MethodNotAllowed() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/delete/" + billId;

        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(get(requestURI))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testAddBillShouldReturn200OK() throws Exception {
        int billId = 123;
        String requestURI = "/api/v1/bills";

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.create(any(BillRequest.class))).thenReturn(bill);

        String requestBody = Util.mapToJson(bill);

        Invoice invoice = Invoice.builder()
                .id(1)
                .paymentMethod(PaymentMethod.CASH)
                .totalAmount(100000L)
                .discount(5000L)
                .sessionId(UUID.randomUUID())
                .build();
        invoiceRepository.save(invoice);

        mockMvc.perform(post(requestURI, invoice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("addBillToInvoice"))
                .andExpect(jsonPath("$.id").value(bill.getId()))
                .andExpect(jsonPath("$.name").value(bill.getName()))
                .andExpect(jsonPath("$.quantity").value(bill.getQuantity()))
                .andExpect(jsonPath("$.price").value(bill.getPrice()))
                .andExpect(jsonPath("$.subTotal").value(bill.getSubTotal()))
                .andDo(print());

        verify(billService, atLeastOnce()).create(any(BillRequest.class));
    }

    @Test
    void testAddBillShouldReturn400BadRequest() throws Exception {
        String requestURI = "/api/v1/bills";

        Bill billFullyEmpty = Bill.builder().build();

        Bill billEmptyName = Bill.builder()
                .name("")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        Bill billEmptyQuantity = Bill.builder()
                .name("Coffee")
                .price(10000)
                .subTotal(50000L)
                .build();

        Bill billEmptyPrice = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .subTotal(50000L)
                .build();

        Bill billEmptySubTotal = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .build();

        Bill billNegativeQuantity = Bill.builder()
                .name("Coffee")
                .quantity(-5)
                .price(10000)
                .subTotal(50000L)
                .build();

        Bill billNegativePrice = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(-10000)
                .subTotal(50000L)
                .build();

        Bill billNegativeSubTotal = Bill.builder()
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(-50000L)
                .build();

        Bill billZeroQuantity = Bill.builder()
                .name("Coffee")
                .quantity(0)
                .price(10000)
                .subTotal(50000L)
                .build();


        String requestBodyFullyEmpty = Util.mapToJson(billFullyEmpty);

        String requestBodyEmptyName = Util.mapToJson(billEmptyName);

        String requestBodyEmptyQuantity = Util.mapToJson(billEmptyQuantity);

        String requestBodyEmptyPrice = Util.mapToJson(billEmptyPrice);

        String requestBodyEmptySubTotal = Util.mapToJson(billEmptySubTotal);

        String requestBodyNegativeQuantity = Util.mapToJson(billNegativeQuantity);

        String requestBodyNegativePrice = Util.mapToJson(billNegativePrice);

        String requestBodyNegativeSubTotal = Util.mapToJson(billNegativeSubTotal);

        String requestBodyZeroQuantity = Util.mapToJson(billZeroQuantity);

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyFullyEmpty))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptyName))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptyQuantity))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptyPrice))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyEmptySubTotal))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyNegativeQuantity))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyNegativePrice))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyNegativeSubTotal))
                .andExpect(status().isBadRequest())
                .andDo(print());

        mockMvc.perform(post(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyZeroQuantity))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void testAddBillShouldReturn405MethodNotAllowed() throws Exception {
        int billId = 123;
        String requestURI = "/api/v1/bills";

        Bill bill = Bill.builder()
                .id(billId)
                .name("Coffee")
                .quantity(5)
                .price(10000)
                .subTotal(50000L)
                .build();

        when(billService.create(any(BillRequest.class))).thenReturn(bill);

        String requestBody = Util.mapToJson(bill);

        mockMvc.perform(get(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(put(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());

        mockMvc.perform(delete(requestURI, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("FAILED"))
                .andDo(print());
    }

    @Test
    void testUpdateBillShouldReturn200OK() throws Exception {
        int billId = 123;
        String requestURI = END_POINT_PATH + "/update/" + billId;

        when(billService.update(any(Integer.class), any(BillRequest.class))).thenReturn(bill);

        String requestBody = Util.mapToJson(billRequest);

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateBillById"))
                .andDo(print());

        verify(billService, atLeastOnce()).update(any(Integer.class), any(BillRequest.class));
    }
}
