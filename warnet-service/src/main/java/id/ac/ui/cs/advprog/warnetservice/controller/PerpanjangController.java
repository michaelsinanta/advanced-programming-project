package id.ac.ui.cs.advprog.warnetservice.controller;

import id.ac.ui.cs.advprog.warnetservice.dto.perpanjang.ExtendSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.model.Session;
import id.ac.ui.cs.advprog.warnetservice.service.perpanjang.PerpanjangServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warnet/perpanjang")
@RequiredArgsConstructor
public class PerpanjangController {
    private final PerpanjangServiceImpl perpanjangService;
    @PostMapping("/tambah_pricing")
    public ResponseEntity<Session> extendSession(@RequestBody ExtendSessionRequest request){
        Session response = perpanjangService.extendSession(request);
        return ResponseEntity.ok(response);
    }
}
