package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.Util;
import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.service.bank.BankServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BankController.class)
@AutoConfigureMockMvc
class BankControllerTest {
    private static final String END_POINT_PATH = "/api/v1/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BankServiceImpl bankService;

    Bank bank;

    BankRequest bankRequest;

    @BeforeEach
    void setUp() {
        bank = Bank.builder()
                .name("BCA")
                .adminFee(6500)
                .build();

        bankRequest = BankRequest.builder()
                .build();
    }

    @Test
    void testGetBankShouldReturn200OK() throws Exception {
        String requestURI = END_POINT_PATH + "banks";

        Bank bank = Bank.builder()
                .id(1)
                .name("BNI")
                .adminFee(5000)
                .build();

        List<Bank> bankList = List.of(bank);

        when(bankService.getAll()).thenReturn(bankList);

        mockMvc.perform(get(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllBanks"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.content[0].id").value(bank.getId()))
                .andDo(print());

        verify(bankService, atLeastOnce()).getAll();
    }

    @Test
    void testCreateBankShouldReturn200OK() throws Exception {
        String requestURI = END_POINT_PATH + "addBank";

        Bank bank = Bank.builder()
                .id(1)
                .name("BCA")
                .adminFee(6500)
                .build();

        String requestBody = Util.mapToJson(bank);

        when(bankService.create(any(BankRequest.class))).thenReturn(bank);

        mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("BCA"))
                .andExpect(jsonPath("$.adminFee").value(6500))
                .andExpect(handler().methodName("addBank"))
                .andDo(print());
        verify(bankService, times(1)).create(any(BankRequest.class));
    }

    @Test
    void testCreateBankShouldReturn400BadRequest () throws Exception {
        String requestURI = END_POINT_PATH + "addBank";

        Bank bankFullyEmpty = Bank.builder().build();

        Bank bankNameEmpty = Bank.builder()
                .name("")
                .adminFee(6500)
                .build();

        Bank bankAdminFeeEmpty = Bank.builder()
                .name("BCA")
                .adminFee(0)
                .build();

        String requestBodyFullyEmpty = Util.mapToJson(bankFullyEmpty);

        String requestBodyNameEmpty = Util.mapToJson(bankNameEmpty);

        String requestBodyAdminFeeEmpty = Util.mapToJson(bankAdminFeeEmpty);

        mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyFullyEmpty))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("addBank"))
                .andDo(print());

        mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyNameEmpty))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("addBank"))
                .andDo(print());

        mockMvc.perform(post(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyAdminFeeEmpty))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("addBank"))
                .andDo(print());

        verify(bankService, times(3)).create(any(BankRequest.class));
    }

    @Test
    void testCreateBankShouldReturn405MethodNotAllowed () throws Exception {
        String requestURI = END_POINT_PATH + "addBank";

        Bank bank = Bank.builder()
                .id(1)
                .name("BCA")
                .adminFee(6500)
                .build();

        String requestBody = Util.mapToJson(bank);

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

        mockMvc.perform(delete(requestURI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    void testCreateBankShouldReturn409Conflict () throws Exception {
        String requestURI = END_POINT_PATH + "addBank";

        Bank bank = Bank.builder()
                .id(1)
                .name("BCA")
                .adminFee(6500)
                .build();

        String requestBody = Util.mapToJson(bank);

        when(bankService.create(any(BankRequest.class))).thenReturn(bank);
        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("BCA"))
                .andExpect(jsonPath("$.adminFee").value(6500))
                .andExpect(handler().methodName("addBank"))
                .andDo(print());
        verify(bankService, times(1)).create(any(BankRequest.class));

        when(bankService.create(any(BankRequest.class))).thenThrow(new BankAlreadyExistsException(bank.getName()));
        mockMvc.perform(post(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(handler().methodName("addBank"))
                .andDo(print());
    }

    @Test
    void testDeleteBankByIdShouldReturn200OK () throws Exception {
        int bankId = 1;
        String requestURI = END_POINT_PATH + "banks/delete/" + bankId;

        Bank bank = Bank.builder()
                .id(bankId)
                .name("BCA")
                .adminFee(6500)
                .build();

        when(bankService.findById(bankId)).thenReturn(bank);

        mockMvc.perform(delete(requestURI))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deleteBankById"))
                .andDo(print());

        verify(bankService, atLeastOnce()).deleteById(bankId);
    }

    @Test
    void testDeleteBankByIdShouldReturn405MethodNotAllowed() throws Exception {
        int bankId = 1;
        String requestURI = END_POINT_PATH + "banks/delete/" + bankId;

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
    void testUpdateBankShouldReturn200OK() throws Exception {
        int bankId = 123;
        String requestURI = END_POINT_PATH + "banks/update/" + bankId;

        when(bankService.update(any(Integer.class), any(BankRequest.class))).thenReturn(bank);

        String requestBody = Util.mapToJson(bankRequest);

        mockMvc.perform(put(requestURI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updateBankById"))
                .andDo(print());

        verify(bankService, atLeastOnce()).update(any(Integer.class), any(BankRequest.class));
    }
}
