package id.ac.ui.cs.advprog.warnetservice.service.sessioninfo;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionDetailResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.GetAllSessionResponse;

@Service
public interface SessionInfoService {
    List<GetAllSessionResponse> getAllSession(Integer page, Integer limit, Integer pcId, String date);
    SessionDetailResponse getSessionDetail(UUID id);
}
