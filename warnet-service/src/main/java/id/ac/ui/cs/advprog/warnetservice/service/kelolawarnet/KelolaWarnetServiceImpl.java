package id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet;

import java.time.LocalDateTime;
import java.util.*;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.SessionDetail;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCDoesNotExistException;
import id.ac.ui.cs.advprog.warnetservice.exceptions.PCIsBeingUsedException;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.repository.PCRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KelolaWarnetServiceImpl implements KelolaWarnetService {
    private final PCRepository pcRepository;
    private final SessionRepository sessionRepository;

    public PC createPC(PCRequest request) {
        if (request.getNoPC() <= 0) {
            throw new NonPositiveParameterException("noPC");
        }
        if (request.getNoRuangan() <= 0) {
            throw new NonPositiveParameterException("noRuangan");
        }
        PC pc = PC.builder()
                .noPC(request.getNoPC())
                .noRuangan(request.getNoRuangan())
                .build();
        return pcRepository.save(pc);
    }

    public List<AllPCResponse> getAllPC(boolean filtered) {
        List<PC> listPC = pcRepository.findAll();
        List<AllPCResponse> response = new ArrayList<>(Collections.emptyList());

        for (PC pc : listPC) {
            boolean beingUsed = isPCBeingUsed(pc.getId());

            // If not filtered, or if filtered AND pc not being used
            if (!filtered || !beingUsed) {
                AllPCResponse resp = AllPCResponse.builder()
                        .id(pc.getId())
                        .noPC(pc.getNoPC())
                        .noRuangan(pc.getNoRuangan())
                        .beingUsed(beingUsed)
                        .activeSession(null)
                        .build();

                Optional<SessionDetail> optionalSessionDetail = getActiveSessionDetail(pc.getId());
                if (optionalSessionDetail.isPresent()) {
                    SessionDetail sessionDetail = optionalSessionDetail.get();
                    resp.setActiveSession(sessionDetail);
                }

                response.add(resp);
            }
        }
        return response;
    }

    public PC getPCById(Integer id) {
        Optional<PC> optionalPC = pcRepository.findById(id);
        if (optionalPC.isEmpty()) {
            throw new PCDoesNotExistException(id);
        }
        return optionalPC.get();
    }

    public PC deletePC(Integer id) {
        PC pc = getPCById(id);
        if (isPCBeingUsed(id)) {
            throw new PCIsBeingUsedException(pc.getNoPC(), pc.getNoRuangan());
        }
        pcRepository.deleteById(id);
        return pc;
    }

    public PC updatePC(Integer id, PCRequest request) {
        Optional<PC> optionalPC = pcRepository.findById(id);
        if (optionalPC.isEmpty()) {
            throw new PCDoesNotExistException(id);
        }
        PC pc = optionalPC.get();
        if (isPCBeingUsed(id)) {
            throw new PCIsBeingUsedException(pc.getNoPC(), pc.getNoRuangan());
        }
        pc.setNoPC(request.getNoPC());
        pc.setNoRuangan(request.getNoRuangan());
        return pcRepository.save(pc);
    }

    private boolean isSessionActive(LocalDateTime datetimeEnd) {
        return datetimeEnd.isAfter(LocalDateTime.now());
    }

    public boolean isPCBeingUsed(Integer id) {
        List<Session> latestSessionList = sessionRepository.getLatestSessionByPcId(id);
        if (latestSessionList.isEmpty()) {
            return false;
        } else {
            return isSessionActive(latestSessionList.get(0).getDatetimeEnd());
        }

    }

    private Optional<SessionDetail> getActiveSessionDetail(Integer id) {
        List<Session> latestSessionList = sessionRepository.getLatestSessionByPcId(id);
        if (latestSessionList.isEmpty()) {
            return Optional.empty();
        } else {
            Session sessionSQL = latestSessionList.get(0);
            LocalDateTime datetimeEnd = sessionSQL.getDatetimeEnd();

            if (isSessionActive(datetimeEnd)) {

                SessionDetail latestSession = SessionDetail.builder()
                        .sessionId(sessionSQL.getId())
                        .datetimeStart(sessionSQL.getDatetimeStart())
                        .datetimeEnd(datetimeEnd)
                        .build();

                return Optional.of(latestSession);
            } else
                return Optional.empty();
        }

    }
}
