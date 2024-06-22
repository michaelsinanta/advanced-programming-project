package id.ac.ui.cs.advprog.bayarservice.exception.coupon;

public class CouponAlreadyUsedException extends RuntimeException {
    public CouponAlreadyUsedException(String couponName) {
        super("Coupon with name " + couponName + " already used");
    }
}
