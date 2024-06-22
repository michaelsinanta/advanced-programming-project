package id.ac.ui.cs.advprog.bayarservice.exception;

public class BillDoesNotExistException extends RuntimeException {
    public BillDoesNotExistException(Integer id) {
        super("Bill with id " + id + " does not exist");
    }
}
