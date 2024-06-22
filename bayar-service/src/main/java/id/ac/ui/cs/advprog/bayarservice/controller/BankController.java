package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.bank.BankRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bank.Bank;
import id.ac.ui.cs.advprog.bayarservice.service.bank.BankService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BankController {
    private final BankService bankService;

    @GetMapping("/banks")
    public ResponseEntity<Object> getAllBanks() {
        List<Bank> banks = bankService.getAll();
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", banks)
        );
    }

    @PostMapping("/addBank")
    public ResponseEntity<Bank> addBank(@RequestBody BankRequest request) {
        Bank bank = bankService.create(request);
        if (bank == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bank);
    }

    @DeleteMapping("/banks/delete/{bankId}")
    public ResponseEntity<String> deleteBankById(@PathVariable Integer bankId) {
        bankService.deleteById(bankId);
        return ResponseEntity.ok(String.format("Deleted Bank with id %d", bankId));
    }

    @PutMapping("banks/update/{bankId}")
    public ResponseEntity<Bank> updateBankById(@PathVariable Integer bankId, @RequestBody BankRequest request) {
        Bank bank = bankService.update(bankId, request);
        return ResponseEntity.ok(bank);
    }
}
