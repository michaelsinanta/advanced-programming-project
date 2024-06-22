package id.ac.ui.cs.advprog.warnetservice.exceptions;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException() {
        super("Format tanggal tidak sesuai. Format yang benar adalah YYYY-mm-dd.");
    }
}
