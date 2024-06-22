package id.ac.ui.cs.advprog.bayarservice.exception.warnet;

import java.util.UUID;

public class SessionDoesNotExistException extends RuntimeException {
    public SessionDoesNotExistException(UUID sessionId) {
        super("Session with id " + sessionId + " does not exist");
    }
}
