package id.ac.ui.cs.advprog.warnetservice.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import id.ac.ui.cs.advprog.warnetservice.Util;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
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
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;

@WebMvcTest(controllers = KelolaWarnetController.class)
@AutoConfigureMockMvc
class KelolaWarnetControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private KelolaWarnetServiceImpl service;

    PC pcHasActive, pcAvailable;
    AllPCResponse notBeingUsedResp;
    AllPCResponse beingUsedResp;
    Object createBodyContent;
    Object updateBodyContent;
    Session activeSess;

    @BeforeEach
    void setUp() {
        pcHasActive = PC.builder()
                .noPC(1)
                .noRuangan(1)
                .build();

        pcAvailable = PC.builder()
                .noPC(2)
                .noRuangan(2)
                .build();

        activeSess = Session.builder()
                .pc(pcHasActive)
                .datetimeStart(LocalDateTime.now().minusMinutes(2))
                .datetimeEnd(LocalDateTime.now().plusHours(2))
                .build();
        pcHasActive.setSessionList(List.of(activeSess));

        notBeingUsedResp = new AllPCResponse(
                pcAvailable.getId(),
                pcAvailable.getNoPC(),
                pcAvailable.getNoRuangan(),
                false,
                null
        );

        beingUsedResp = new AllPCResponse(
                pcHasActive.getId(),
                pcHasActive.getNoPC(),
                pcHasActive.getNoRuangan(),
                true,
                new SessionDetail(
                        activeSess.getId(),
                        activeSess.getDatetimeStart(),
                        activeSess.getDatetimeEnd()
                )
        );


        createBodyContent = new Object() {
            public final Integer noPC = 1;
            public final Integer noRuangan = 1;
        };

        updateBodyContent = new Object(){
            public final Integer noPC = 2;
            public final Integer noRuangan = 1;
        };
    }

    @Test
    void testGetAllPCNotFilteredShouldReturnBothPC() throws Exception {
        List<AllPCResponse> allPC = new ArrayList<>();
        allPC.add(beingUsedResp);
        allPC.add(notBeingUsedResp);

        when(service.getAllPC(false)).thenReturn(allPC);

        mvc.perform(get("/warnet/kelola_warnet/all_pc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPC"))
                .andExpect(jsonPath("$[0].noPC").value(pcHasActive.getNoPC()))
                .andExpect(jsonPath("$[0].beingUsed").value(true))
                .andExpect(jsonPath("$[0].activeSession.sessionId").value(activeSess.getId()))
                .andExpect(jsonPath("$[1].noPC").value(pcAvailable.getNoPC()))
                .andExpect(jsonPath("$[1].beingUsed").value(false))
                .andExpect(jsonPath("$[1].activeSession").value(IsNull.nullValue()));

        verify(service, atLeastOnce()).getAllPC(false);
    }

    @Test
    void testGetAllPCFilteredArgumentFalseShouldReturnBothPC() throws Exception {
        List<AllPCResponse> allPC = new ArrayList<>();
        allPC.add(beingUsedResp);
        allPC.add(notBeingUsedResp);

        when(service.getAllPC(false)).thenReturn(allPC);

        mvc.perform(get("/warnet/kelola_warnet/all_pc?filtered=false")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPC"))
                .andExpect(jsonPath("$[0].noPC").value(pcHasActive.getNoPC()))
                .andExpect(jsonPath("$[0].beingUsed").value(true))
                .andExpect(jsonPath("$[0].activeSession.sessionId").value(activeSess.getId()))
                .andExpect(jsonPath("$[1].noPC").value(pcAvailable.getNoPC()))
                .andExpect(jsonPath("$[1].beingUsed").value(false))
                .andExpect(jsonPath("$[1].activeSession").value(IsNull.nullValue()));

        verify(service, atLeastOnce()).getAllPC(false);
    }

    @Test
    void testGetAllPCFilteredShouldReturnAvailablePC() throws Exception {
        List<AllPCResponse> allPC = List.of(notBeingUsedResp);

        when(service.getAllPC(true)).thenReturn(allPC);

        mvc.perform(get("/warnet/kelola_warnet/all_pc?filtered=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPC"))
                .andExpect(jsonPath("$[0].noPC").value(pcAvailable.getNoPC()))
                .andExpect(jsonPath("$[0].beingUsed").value(false))
                .andExpect(jsonPath("$[0].activeSession").value(IsNull.nullValue()));

        verify(service, atLeastOnce()).getAllPC(true);
    }

    @Test
    void testGetAllPCNotFilteredEmpty() throws Exception {
        List<AllPCResponse> allPC = List.of();

        when(service.getAllPC(false)).thenReturn(allPC);

        mvc.perform(get("/warnet/kelola_warnet/all_pc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPC"))
                .andExpect(jsonPath("$").isEmpty());

        verify(service, atLeastOnce()).getAllPC(false);
    }

    @Test
    void testGetAllPCFilteredEmpty() throws Exception {
        List<AllPCResponse> allPC = List.of();

        when(service.getAllPC(true)).thenReturn(allPC);

        mvc.perform(get("/warnet/kelola_warnet/all_pc?filtered=true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllPC"))
                .andExpect(jsonPath("$").isEmpty());

        verify(service, atLeastOnce()).getAllPC(true);
    }

    @Test
    void testGetPCByID() throws Exception {
        when(service.getPCById(anyInt())).thenReturn(pcHasActive);

        mvc.perform(get("/warnet/kelola_warnet/pc_detail/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getPCById"))
                .andExpect(jsonPath("$.noPC").value(pcHasActive.getNoPC()));

        verify(service, atLeastOnce()).getPCById(pcHasActive.getNoPC());
    }

    @Test
    void testGetPCByIDFailedShouldReturnNotFound() throws Exception{
        when(service.getPCById(anyInt())).thenThrow(new PCDoesNotExistException(anyInt()));

        mvc.perform(get("/warnet/kelola_warnet/pc_detail/100")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("getPCById"));

        verify(service,atLeastOnce()).getPCById(anyInt());
    }


    @Test
    void testDeletePC() throws Exception {
        mvc.perform(delete("/warnet/kelola_warnet/delete_pc/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("deletePC"));

        verify(service, atLeastOnce()).deletePC(any(Integer.class));
    }
    @Test
    void testDeletePCFailedShouldReturnForbidden() throws Exception{
        pcHasActive.setId(1);
        int idPC = pcHasActive.getId();
        when(service.deletePC(idPC)).thenThrow(new PCIsBeingUsedException(pcHasActive.getNoPC(), pcHasActive.getNoRuangan()));
        mvc.perform(delete(String.format("/warnet/kelola_warnet/delete_pc/%d",idPC))
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(handler().methodName("deletePC"));

        verify(service,atLeastOnce()).deletePC(any(Integer.class));
    }

    @Test
    void testDeletePCFailedShouldReturnNotFound()throws Exception{
        when(service.deletePC(100)).thenThrow(new PCDoesNotExistException(100));
        mvc.perform(delete("/warnet/kelola_warnet/delete_pc/100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("deletePC"));

        verify(service,atLeastOnce()).deletePC(any(Integer.class));
    }

    @Test
    void testCreatePCValidShouldSucceed() throws Exception {
        when(service.createPC(any(PCRequest.class))).thenReturn(pcAvailable);

        mvc.perform(post("/warnet/kelola_warnet/create_pc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(createBodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("createPC"))
                .andExpect(jsonPath("$.noPC").value(pcAvailable.getNoPC()));

        verify(service, atLeastOnce()).createPC(any(PCRequest.class));
    }

    @Test
    void testFailedCreatePCShouldThrowBadRequest() throws Exception {
        when(service.createPC(any(PCRequest.class))).thenThrow(new NonPositiveParameterException("noPC"));

        mvc.perform(post("/warnet/kelola_warnet/create_pc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Util.mapToJson(createBodyContent)))
                .andExpect(status().isBadRequest())
                .andExpect(handler().methodName("createPC"));

        verify(service, atLeastOnce()).createPC(any(PCRequest.class));
    }

    @Test
    void testUpdatePC() throws Exception{
        when(service.updatePC(anyInt(), any(PCRequest.class))).thenReturn(pcAvailable);

        mvc.perform(put("/warnet/kelola_warnet/pc_detail/1/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Util.mapToJson(updateBodyContent)))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("updatePC"))
                .andExpect(jsonPath("$.noPC").value(pcAvailable.getNoPC()));

        verify(service,atLeastOnce()).updatePC(anyInt(),any(PCRequest.class));
    }

    @Test
    void testUpdatePCFailedPCNotfoundShouldReturnNotFound() throws Exception{
        when(service.updatePC(anyInt(), any(PCRequest.class))).thenThrow(new PCDoesNotExistException(100));

        mvc.perform(put("/warnet/kelola_warnet/pc_detail/100/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Util.mapToJson(updateBodyContent)))
                .andExpect(status().isNotFound())
                .andExpect(handler().methodName("updatePC"));

        verify(service,atLeastOnce()).updatePC(anyInt(), any(PCRequest.class));
    }

    @Test
    void testUpdatePCnoPCisInvalidShouldReturnForbidden()throws Exception{
        pcHasActive.setId(1);
        when(service.updatePC(anyInt(), any(PCRequest.class))).thenThrow(new PCIsBeingUsedException(pcHasActive.getNoPC(), pcHasActive.getNoRuangan()));

        mvc.perform(put("/warnet/kelola_warnet/pc_detail/1/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Util.mapToJson(updateBodyContent)))
                .andExpect(status().isForbidden())
                .andExpect(handler().methodName("updatePC"));

        verify(service,atLeastOnce()).updatePC(anyInt(), any(PCRequest.class));
    }

}
