package id.ac.ui.cs.advprog.warnetservice.service.sessioninfo;

import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.GetAllSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionDetailResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionPricingResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.*;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.model.SessionPricing;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionPricingRepository;
import id.ac.ui.cs.advprog.warnetservice.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SessionInfoServiceImpl implements SessionInfoService {
    private final SessionRepository sessionRepository;
    private final SessionPricingRepository sessionPricingRepository;

    @Override
    public List<GetAllSessionResponse> getAllSession(Integer page, Integer limit, Integer pcId, String date) {
        int offset = (page-1) * limit;
        var sessions = sessionRepository.listSession(offset, limit, pcId, date);
        List<GetAllSessionResponse> response = new ArrayList<>();
        for (Session session: sessions) {
            GetAllSessionResponse resp = GetAllSessionResponse.builder()
                    .id(session.getId())
                    .pcId(session.getPc().getId())
                    .noPC(session.getPc().getNoPC())
                    .noRuangan(session.getPc().getNoRuangan())
                    .datetimeStart(session.getDatetimeStart())
                    .datetimeEnd(session.getDatetimeEnd())
                    .build();

            response.add(resp);
        }
        return response;
    }

    public SessionDetailResponse getSessionDetail(UUID id) {
        SessionDetailResponse sessionDetailResponse = new SessionDetailResponse();
        Optional<Session> optionalSession = sessionRepository.findById(id);
        if (optionalSession.isEmpty()) {
            throw new SessionDoesNotExistException(id.toString());
        }
        Session session = optionalSession.get();
        sessionDetailResponse.setSession(session);
        List<SessionPricing> sessionPricingList = sessionPricingRepository.findBySession(session);
        List<SessionPricingResponse> sessionPricingResponseList = new ArrayList<>();
        for (SessionPricing sessionPricing : sessionPricingList) {
            sessionPricingResponseList.add(new SessionPricingResponse(sessionPricing.getPricing(),
                    sessionPricing.getWaktuPembelian(), sessionPricing.getQuantity()));
        }
        sessionDetailResponse.setSessionPricingList(sessionPricingResponseList);
        return sessionDetailResponse;
    }
}
