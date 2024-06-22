package id.ac.ui.cs.advprog.warnetservice.exceptions;
public class PricingDoesNotExistException extends RuntimeException{
    public PricingDoesNotExistException(Integer id) {
        super("Pricing dengan id " + id + " tidak ada");
    }
}
