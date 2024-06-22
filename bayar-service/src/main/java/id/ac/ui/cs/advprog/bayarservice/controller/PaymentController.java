package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.payment.DetailPaymentLogResponse;
import id.ac.ui.cs.advprog.bayarservice.dto.payment.PaymentRequest;
import id.ac.ui.cs.advprog.bayarservice.model.payment.PaymentLog;
import id.ac.ui.cs.advprog.bayarservice.service.payment.PaymentServiceImpl;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentService;
    private static final String SUCCESS = "SUCCESS";
    private static final String RETRIEVED = "Success retrieved data";

    private static final String ERROR = "ERROR";

    @GetMapping("/methods")
    public ResponseEntity<Object> getPaymentMethods() {
        List<String> response = paymentService.getPaymentMethods();
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }

    @PostMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<Object> createPayment(@PathVariable Integer invoiceId, @RequestBody @Valid PaymentRequest request) {
        PaymentLog paymentLog = paymentService.create(invoiceId, request);
        return ResponseHandler.generateResponse(new Response(
                "Payment processed successfully!", HttpStatus.CREATED, SUCCESS, paymentLog)
        );
    }

    @GetMapping("/log/paymentLog")
    public ResponseEntity<Object> getPaymentLog() {
        List<PaymentLog> response = paymentService.getPaymentLog();
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }

    @GetMapping("/log/paymentLog/monthly/{year}/{month}")
    public ResponseEntity<Object> getPaymentLogByYearAndMonth(@PathVariable Integer year, @PathVariable Integer month) {
        if (year < 0 || month < 0 || month > 12) {
            return ResponseHandler.generateResponse(new Response(
                    "Year must be greater than 0 and month must be between 1 and 12", HttpStatus.BAD_REQUEST, ERROR, null)
            );
        }
        List<PaymentLog> response = paymentService.getPaymentLogByYearAndMonth(year, month);
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }

    @GetMapping("/log/paymentLog/{year}")
    public ResponseEntity<Object> getPaymentLogByYear(@PathVariable Integer year) {
        if (year < 0) {
            return ResponseHandler.generateResponse(new Response(
                    "Year must be greater than 0", HttpStatus.BAD_REQUEST, ERROR, null)
            );
        }
        List<PaymentLog> response = paymentService.getPaymentLogByYear(year);
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }

    @GetMapping("/log/paymentLog/weekly/{year}/{week}")
    public ResponseEntity<Object> getPaymentLogByWeekAndYear(@PathVariable Integer year, @PathVariable Integer week) {
        if (year < 0 || week < 0 || week > 52) {
            return ResponseHandler.generateResponse(new Response(
                    "Year must be greater than 0 and week must be between 1 and 52", HttpStatus.BAD_REQUEST, ERROR, null)
            );
        }
        List<PaymentLog> response = paymentService.getPaymentLogByWeekAndYear(year, week);
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }

    @GetMapping("/log/paymentLog/detail/{sessionId}")
    public ResponseEntity<Object> getPaymentLogDetail(@PathVariable UUID sessionId) {
        DetailPaymentLogResponse response = paymentService.getPaymentLogDetail(sessionId);
        return ResponseHandler.generateResponse(new Response(
                RETRIEVED, HttpStatus.OK, SUCCESS, response)
        );
    }
}
