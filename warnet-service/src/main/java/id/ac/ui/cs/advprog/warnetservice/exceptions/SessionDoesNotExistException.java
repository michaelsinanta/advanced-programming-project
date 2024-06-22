package id.ac.ui.cs.advprog.warnetservice.exceptions;

public class SessionDoesNotExistException extends RuntimeException{
    public SessionDoesNotExistException(String uuid) {
        super("Session dengan UUID " + uuid + " tidak ada");
    }
}
