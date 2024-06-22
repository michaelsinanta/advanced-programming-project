package id.ac.ui.cs.advprog.warnetservice.controller;

import org.springframework.web.bind.annotation.*;

import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.AllPCResponse;
import id.ac.ui.cs.advprog.warnetservice.dto.kelolawarnet.PCRequest;
import id.ac.ui.cs.advprog.warnetservice.model.PC;
import id.ac.ui.cs.advprog.warnetservice.service.kelolawarnet.KelolaWarnetServiceImpl;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/warnet/kelola_warnet")
@RequiredArgsConstructor
public class KelolaWarnetController {
    private final KelolaWarnetServiceImpl kelolaWarnetService;

    @PostMapping("/create_pc")
    public ResponseEntity<PC> createPC(@RequestBody PCRequest request) {
        PC response = kelolaWarnetService.createPC(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all_pc")
    public ResponseEntity<List<AllPCResponse>> getAllPC(@RequestParam Optional<Boolean> filtered) {
        List<AllPCResponse> response = kelolaWarnetService.getAllPC(filtered.isPresent() && filtered.get());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pc_detail/{id}")
    public ResponseEntity<PC> getPCById(@PathVariable Integer id) {
        PC response = kelolaWarnetService.getPCById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete_pc/{id}")
    public ResponseEntity<PC> deletePC(@PathVariable Integer id) {
        PC response = kelolaWarnetService.deletePC(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/pc_detail/{id}/update")
    public ResponseEntity<PC> updatePC(@PathVariable Integer id, @RequestBody PCRequest request){
        PC response = kelolaWarnetService.updatePC(id, request);
        return ResponseEntity.ok(response);
    }

}
