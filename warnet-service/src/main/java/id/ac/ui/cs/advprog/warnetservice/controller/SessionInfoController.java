package id.ac.ui.cs.advprog.warnetservice.controller;

import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.GetAllSessionResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.sessioninfo.SessionDetailResponse;
import id.ac.ui.cs.advprog.warnetservice.exceptions.InvalidDateException;
import id.ac.ui.cs.advprog.warnetservice.service.sessioninfo.SessionInfoServiceImpl;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warnet/info_sesi")
@RequiredArgsConstructor
public class SessionInfoController {
    private final SessionInfoServiceImpl sessionInfoService;

    @GetMapping("/all_session")
    public ResponseEntity<List<GetAllSessionResponse>> getAllSession(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "limit") Integer limit,
            @RequestParam(value = "pcId", defaultValue = "null") String pcId,
            @RequestParam(value = "date", defaultValue = "null") String date) {

        Integer parsedPcId = pcId.equals("null") ? null : Integer.parseInt(pcId);
        if (date.equals("null")) {
            date = null;
        } else {
            try {
                Date.valueOf(date);
            } catch (Exception e) {
                throw new InvalidDateException();
            }
        }

        List<GetAllSessionResponse> response = sessionInfoService.getAllSession(page, limit, parsedPcId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/session_detail/{id}")
    public ResponseEntity<SessionDetailResponse> getSessionById(@PathVariable UUID id) {
        SessionDetailResponse response = sessionInfoService.getSessionDetail(id);
        return ResponseEntity.ok(response);
    }

}