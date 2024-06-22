package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.bill.BillRequest;
import id.ac.ui.cs.advprog.bayarservice.model.bill.Bill;
import id.ac.ui.cs.advprog.bayarservice.service.bill.BillService;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BillController {
    private final BillService billService;

    @GetMapping("/bills/{billId}")
    public ResponseEntity<Object> getBillById(@PathVariable Integer billId) {
        Bill bill = billService.findById(billId);
        return ResponseHandler.generateResponse(new Response(
                "Success retrieved data", HttpStatus.OK, "SUCCESS", bill)
        );
    }

    @PostMapping("/bills")
    public ResponseEntity<Bill> addBillToInvoice(@RequestBody BillRequest request) {
        if (
                request.getName() == null ||
                request.getName().equals("") ||
                request.getQuantity() == null ||
                request.getPrice() == null ||
                request.getSubTotal() == null ||
                request.toString().equals("") ||
                request.getPrice() < 0 ||
                request.getQuantity() < 1 ||
                request.getSubTotal() < 0
        ) {
            return ResponseEntity.badRequest().build();
        } else {
            Bill response = billService.create(request);
            if (response == null) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.ok(response);
        }
    }

    @DeleteMapping("/bills/delete/{billId}")
    public ResponseEntity<String> deleteBillById(@PathVariable Integer billId) {
        billService.delete(billId);
        return ResponseEntity.ok(String.format("Deleted Bill with id %d", billId));
    }

    @PutMapping("/bills/update/{billId}")
    public ResponseEntity<Bill> updateBillById(@PathVariable Integer billId, @RequestBody BillRequest request) {
        Bill updatedBill = billService.update(billId, request);
        return ResponseEntity.ok(updatedBill);
    }
}
