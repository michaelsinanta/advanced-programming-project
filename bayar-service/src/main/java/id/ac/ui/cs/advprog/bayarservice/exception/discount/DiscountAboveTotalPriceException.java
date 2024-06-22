package id.ac.ui.cs.advprog.bayarservice.exception.discount;

public class DiscountAboveTotalPriceException extends RuntimeException {
    public DiscountAboveTotalPriceException() {
        super("Discount cannot be larger than the final total");
    }
}
