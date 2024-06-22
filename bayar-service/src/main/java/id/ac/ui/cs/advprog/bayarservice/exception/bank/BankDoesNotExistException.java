package id.ac.ui.cs.advprog.bayarservice.exception.bank;

public class BankDoesNotExistException extends RuntimeException {
    public BankDoesNotExistException(Integer id) {
        super("Bank with id " + id + " does not exist");
    }
}
