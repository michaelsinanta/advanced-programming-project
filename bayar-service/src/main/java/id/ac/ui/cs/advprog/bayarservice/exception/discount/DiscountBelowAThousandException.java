package id.ac.ui.cs.advprog.bayarservice.exception.discount;

public class DiscountBelowAThousandException extends RuntimeException {
    public DiscountBelowAThousandException() {
        super("Discount cannot be under Rp1.000");
    }
}
