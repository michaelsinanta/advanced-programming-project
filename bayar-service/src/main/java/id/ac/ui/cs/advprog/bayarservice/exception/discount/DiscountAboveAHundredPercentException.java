package id.ac.ui.cs.advprog.bayarservice.exception.discount;

public class DiscountAboveAHundredPercentException extends RuntimeException {
    public DiscountAboveAHundredPercentException() {
        super("Discount cannot be above 100%");
    }
}
