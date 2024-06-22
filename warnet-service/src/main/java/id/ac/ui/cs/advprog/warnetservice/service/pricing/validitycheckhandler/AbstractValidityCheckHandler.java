package id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler;

import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;

@Component
public abstract class AbstractValidityCheckHandler {
    private AbstractValidityCheckHandler nextHandler;


    public AbstractValidityCheckHandler setNextHandler(AbstractValidityCheckHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this.nextHandler;
    }

    public void callNextHandler(CreateSessionRequest createSessionRequest) {
        if (nextHandler != null) {
            nextHandler.handleRequest(createSessionRequest);
        }
    }
    public abstract void handleRequest(CreateSessionRequest createSessionRequest);
}

