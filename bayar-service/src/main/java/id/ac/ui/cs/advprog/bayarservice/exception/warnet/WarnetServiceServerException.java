package id.ac.ui.cs.advprog.bayarservice.exception.warnet;

public class WarnetServiceServerException extends RuntimeException {
    public WarnetServiceServerException(String message) {
        super("Internal Warnet Server error: " + message);
    }
}
