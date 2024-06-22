package id.ac.ui.cs.advprog.warnetservice.service;

import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;
import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class KelolaWarnetServiceImplTest {

    @InjectMocks
    private KelolaWarnetServiceImpl service;

    @Mock
    private PCRepository repository;

    @Mock
    private SessionRepository sessionRepository;

    PC pc, pcHasActive, pcAvailable, pcAvailable2;
    AllPCResponse notBeingUsedResp, beingUsedResp, notBeingUsedResp2;
    PCRequest validCreateRequest, invalidNoPCCreateRequest, invalidNoRuanganCreateRequest;
    Session session, endedSession;
    PCRequest updateRequest;

    @BeforeEach
    void setUp() {
        pc = PC.builder()
                .noPC(1)
                .noRuangan(1)
                .build();
        pc.setId(0);

        pcHasActive = PC.builder()
                .noPC(1)
                .noRuangan(2)
                .build();
        pcHasActive.setId(1);

        pcAvailable = PC.builder()
                .noPC(2)
                .noRuangan(2)
                .build();
        pcAvailable.setId(2);

        pcAvailable2 = PC.builder()
                .noPC(3)
                .noRuangan(2)
                .build();
        pcAvailable2.setId(3);

        session = Session.builder()
                .pc(pcHasActive)
                .datetimeStart(LocalDateTime.now())
                .datetimeEnd(LocalDateTime.now().plusHours(2))
                .build();
        pcHasActive.setSessionList(List.of(session));

        endedSession = Session.builder()
                .pc(pcAvailable2)
                .datetimeStart(LocalDateTime.now().minusHours(2))
                .datetimeEnd(LocalDateTime.now().minusHours(1))
                .build();
        pcAvailable2.setSessionList(List.of(endedSession));

        pcAvailable.setSessionList(Collections.emptyList());

        notBeingUsedResp = new AllPCResponse(
                pcAvailable.getId(),
                pcAvailable.getNoPC(),
                pcAvailable.getNoRuangan(),
                false,
                null
        );

        notBeingUsedResp2 = new AllPCResponse(
                pcAvailable2.getId(),
                pcAvailable2.getNoPC(),
                pcAvailable2.getNoRuangan(),
                false,
                null
        );

        beingUsedResp = new AllPCResponse(
                pcHasActive.getId(),
                pcHasActive.getNoPC(),
                pcHasActive.getNoRuangan(),
                true,
                new SessionDetail(
                        session.getId(),
                        session.getDatetimeStart(),
                        session.getDatetimeEnd()
                )
        );

        validCreateRequest = PCRequest.builder()
                .noPC(1)
                .noRuangan(1)
                .build();

        invalidNoPCCreateRequest = PCRequest.builder()
                .noPC(-1)
                .noRuangan(1)
                .build();

        invalidNoRuanganCreateRequest = PCRequest.builder()
                .noPC(1)
                .noRuangan(-1)
                .build();

        updateRequest = PCRequest.builder()
                .noPC(2)
                .noRuangan(1)
                .build();

    }

    @Test
    void testGetAllPCNotFilteredShouldReturnAllPC() {
        List<PC> allPC = List.of(pcHasActive, pcAvailable, pcAvailable2);

        List<AllPCResponse> expected = List.of(beingUsedResp, notBeingUsedResp, notBeingUsedResp2);

        when(repository.findAll()).thenReturn(allPC);
        when(sessionRepository.getLatestSessionByPcId(1)).thenReturn(pcHasActive.getSessionList());
        when(sessionRepository.getLatestSessionByPcId(2)).thenReturn(pcAvailable.getSessionList());
        when(sessionRepository.getLatestSessionByPcId(3)).thenReturn(pcAvailable2.getSessionList());

        List<AllPCResponse> result = service.getAllPC(false);

        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllPCFilteredShouldReturnAvailablePC() {
        List<PC> allPC = List.of(pcHasActive, pcAvailable);

        List<AllPCResponse> expected = List.of(notBeingUsedResp);

        when(repository.findAll()).thenReturn(allPC);
        when(sessionRepository.getLatestSessionByPcId(1)).thenReturn(pcHasActive.getSessionList());
        when(sessionRepository.getLatestSessionByPcId(2)).thenReturn(pcAvailable.getSessionList());

        List<AllPCResponse> result = service.getAllPC(true);

        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllPCNotFilteredShouldReturnEmpty() {
        List<AllPCResponse> expected = Collections.emptyList();

        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<AllPCResponse> result = service.getAllPC(false);

        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllPCFilteredNonEmptyRepoShouldReturnEmpty() {
        List<AllPCResponse> expected = Collections.emptyList();

        when(repository.findAll()).thenReturn(List.of(pcHasActive));
        when(sessionRepository.getLatestSessionByPcId(1)).thenReturn(pcHasActive.getSessionList());

        List<AllPCResponse> result = service.getAllPC(true);

        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetAllPCFilteredEmptyRepoShouldReturnEmpty() {
        List<AllPCResponse> expected = Collections.emptyList();

        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<AllPCResponse> result = service.getAllPC(true);

        verify(repository, atLeastOnce()).findAll();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetPCByIdSuccess() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(pc));

        PC result = service.getPCById(pc.getId());
        verify(repository, atLeastOnce()).findById(pc.getId());
        Assertions.assertEquals(pc, result);
    }

    @Test
    void testGetPCByIdEmpty() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());

        try {
            service.getPCById(pc.getId());
        }
        catch (Exception e) {
            String expectedMessage = "PC dengan id 0 tidak ada";
            String actualMessage = e.getMessage();

            Assertions.assertEquals(expectedMessage, actualMessage);
        }
        verify(repository, atLeastOnce()).findById(pc.getId());
    }

    @Test
    void testCreatePCValidShouldReturnTheCreatedPC() {
        when(repository.save(any(PC.class))).thenAnswer(invocation -> {
            var pc = invocation.getArgument(0, PC.class);
            pc.setId(0);
            return pc;
        });

        PC result = service.createPC(validCreateRequest);
        verify(repository, atLeastOnce()).save(any(PC.class));
        Assertions.assertEquals(pc, result);
    }

    @Test
    void testCreatePCInvalidNoPCShouldFail() {
        NonPositiveParameterException e = assertThrows(
                NonPositiveParameterException.class,
                () -> service.createPC(invalidNoPCCreateRequest)
        );

        assertEquals("Parameter noPC harus positif", e.getMessage());
        verify(repository, times(0)).save(any(PC.class));
    }

    @Test
    void testCreatePCInvalidNoRuanganShouldFail() {
        NonPositiveParameterException e = assertThrows(
                NonPositiveParameterException.class,
                () -> service.createPC(invalidNoRuanganCreateRequest)
        );

        assertEquals("Parameter noRuangan harus positif", e.getMessage());
        verify(repository, times(0)).save(any(PC.class));
    }

    @Test
    void testUpdateSuccess(){
        pc.setSessionList(Collections.emptyList());
        when(repository.findById(anyInt())).thenReturn(Optional.of(pc));
        when(repository.save(pc)).thenReturn(pc);

        PC updatedPC = service.updatePC(anyInt(), updateRequest);

        assertEquals(updateRequest.getNoPC(), updatedPC.getNoPC());
        assertEquals(updateRequest.getNoRuangan(), updatedPC.getNoRuangan());
        verify(repository, times(1)).findById(anyInt());
        verify(repository, atLeastOnce()).save(pc);
    }

    @Test
    void testUpdateFailPCIsBeingUsed(){
        pc.setSessionList(List.of(session));
        when(repository.findById(anyInt())).thenReturn(Optional.of(pc));
        when(repository.findById(anyInt())).thenReturn(Optional.of(pc));
        when(sessionRepository.getLatestSessionByPcId(anyInt())).thenReturn(pcHasActive.getSessionList());

        Integer anyInteger = anyInt();

        assertThrows(PCIsBeingUsedException.class, () -> service.updatePC(anyInteger, updateRequest));
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdatePCFailPCDOesNotExist(){
        Integer anyInteger = anyInt();
        when(repository.findById(anyInteger)).thenReturn(Optional.empty());

        assertThrows(PCDoesNotExistException.class, () -> service.updatePC(anyInteger, updateRequest));
        verify(repository, times(1)).findById(anyInt());
        verify(repository, never()).save(any());
    }

    @Test
    void testDeletePCSuccess() {
        pc.setSessionList(Collections.emptyList());
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(pc));

        service.deletePC(0);
        verify(repository, atLeastOnce()).deleteById(any(Integer.class));
    }

    @Test
    void testDeletePCAndIsBeingUsedShouldThrowException() {
        when(repository.findById(any(Integer.class))).thenReturn(Optional.of(pcHasActive));
        when(sessionRepository.getLatestSessionByPcId(pcHasActive.getId())).thenReturn(pcHasActive.getSessionList());
        Integer pcId = pcHasActive.getId();
        assertThrows(PCIsBeingUsedException.class, () -> {
            service.deletePC(pcId);
        });
    }

    @Test
    void testLatestSessionAlreadyEnded() {
        session = Session.builder()
                .pc(pc)
                .datetimeStart(LocalDateTime.now().minusHours(2))
                .datetimeEnd(LocalDateTime.now().minusHours(1))
                .build();
        when(sessionRepository.getLatestSessionByPcId(anyInt())).thenReturn(List.of(session));
        Integer pcId = pc.getId();
        assertEquals(false, service.isPCBeingUsed(pcId));
    }
}
