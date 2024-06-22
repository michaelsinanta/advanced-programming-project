package id.ac.ui.cs.advprog.bayarservice.exception.coupon;

public class CouponDoesNotExistException extends RuntimeException {
    public CouponDoesNotExistException(Integer couponId) {
        super("Coupon with id " + couponId + " does not exist");
    }

    public CouponDoesNotExistException(String couponName) {
        super("Coupon with name " + couponName + " does not exist");
    }
}
