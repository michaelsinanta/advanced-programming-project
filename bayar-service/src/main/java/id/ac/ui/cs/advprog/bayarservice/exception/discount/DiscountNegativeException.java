package id.ac.ui.cs.advprog.bayarservice.exception.discount;

public class DiscountNegativeException extends RuntimeException {
    public DiscountNegativeException() {
        super("Discount cannot be negative");
    }
}
