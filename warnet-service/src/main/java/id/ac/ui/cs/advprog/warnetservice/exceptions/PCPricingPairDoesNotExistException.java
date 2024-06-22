package id.ac.ui.cs.advprog.warnetservice.exceptions;
public class PCPricingPairDoesNotExistException extends RuntimeException{
    public PCPricingPairDoesNotExistException(Integer pcId, Integer pricingId) {
        super("PC dengan id " + pcId + " tidak memiliki pricing dengan id " + pricingId);
    }
}
