package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.discount.DiscountRequest;
import id.ac.ui.cs.advprog.bayarservice.service.discount.DiscountService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountService discountService;

    @PostMapping("/sessions/{sessionId}/discount")
    public ResponseEntity<Object> giveDiscount(@PathVariable UUID sessionId, @RequestBody @Valid DiscountRequest request) {
        discountService.giveDiscount(sessionId, request);
        return ResponseHandler.generateResponse(new Response(
                "Successfully given discount", HttpStatus.OK, "SUCCESS", null)
        );
    }
}
