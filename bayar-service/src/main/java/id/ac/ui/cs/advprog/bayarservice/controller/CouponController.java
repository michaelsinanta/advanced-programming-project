package id.ac.ui.cs.advprog.bayarservice.controller;

import id.ac.ui.cs.advprog.bayarservice.dto.coupon.CouponRequest;
import id.ac.ui.cs.advprog.bayarservice.dto.coupon.UseCouponRequest;
import id.ac.ui.cs.advprog.bayarservice.model.coupon.Coupon;
import id.ac.ui.cs.advprog.bayarservice.service.coupon.CouponService;
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
public class CouponController {
    private final CouponService couponService;
    private static final String SUCCESS = "SUCCESS";

    private static final String FAILED = "FAILED";

    @PutMapping("/coupons/{couponId}")
    public ResponseEntity<Object> updateCoupon(@PathVariable Integer couponId, @RequestBody @Valid CouponRequest request) {
        Coupon coupon = couponService.update(couponId, request);
        return ResponseHandler.generateResponse(new Response(
                "Success created invoice", HttpStatus.OK, SUCCESS, coupon)
        );
    }

    @PostMapping("/sessions/{sessionId}/coupons/use")
    public ResponseEntity<Object> useCoupon(@PathVariable UUID sessionId, @RequestBody @Valid UseCouponRequest request) {
        couponService.useCoupon(sessionId, request);
        return ResponseHandler.generateResponse(new Response(
                "Success Used Coupon", HttpStatus.OK, SUCCESS, null)
        );
    }

    @PostMapping("/coupons/createCoupon")
    public ResponseEntity<Object> createCoupon(@RequestBody @Valid CouponRequest request) {
        if (request.getName().equals("")) {
            return ResponseHandler.generateResponse(new Response(
                    "Coupon Name Is Mandatory", HttpStatus.BAD_REQUEST, FAILED, null)
            );
        }
        Coupon coupon = couponService.createCoupon(request);
        return ResponseHandler.generateResponse(new Response(
                "Success created coupon", HttpStatus.CREATED, SUCCESS, coupon)
        );
    }


    @DeleteMapping("/coupons/delete/{couponId}")
    public ResponseEntity<Object> deleteCoupon(@PathVariable Integer couponId) {
        couponService.deleteCoupon(couponId);
        return ResponseHandler.generateResponse(new Response(
                "Success deleted coupon", HttpStatus.OK, SUCCESS, null)
        );
    }

    @GetMapping("/coupons/getAll")
    public ResponseEntity<Object> getAllCoupon() {
        return ResponseHandler.generateResponse( new Response(
                "Success retrieved all coupons", HttpStatus.OK, SUCCESS, couponService.getAllCoupon()
        ));
    }
}
