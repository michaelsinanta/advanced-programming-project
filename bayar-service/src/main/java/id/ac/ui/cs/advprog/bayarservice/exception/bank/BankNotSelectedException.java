package id.ac.ui.cs.advprog.bayarservice.exception.bank;

public class BankNotSelectedException extends RuntimeException {
    public BankNotSelectedException() {
        super("You have not selected a bank");
    }
}
