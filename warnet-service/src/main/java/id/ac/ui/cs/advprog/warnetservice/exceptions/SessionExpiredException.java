package id.ac.ui.cs.advprog.warnetservice.exceptions;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException (String uuid) {
        super("Session dengan UUID " + uuid + " telah expired");
    }
}
