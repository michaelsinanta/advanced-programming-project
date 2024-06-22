package id.ac.ui.cs.advprog.bayarservice.exception.coupon;

public class CouponAlreadyExistException extends RuntimeException{
    public CouponAlreadyExistException(String name) {
        super("Bank with name " + name + " already exists");
    }
}
