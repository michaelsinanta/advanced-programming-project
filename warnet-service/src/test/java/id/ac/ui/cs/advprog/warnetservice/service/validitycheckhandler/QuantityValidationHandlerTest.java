package id.ac.ui.cs.advprog.warnetservice.service.validitycheckhandler;

import id.ac.ui.cs.advprog.warnetservice.dto.pricing.CreateSessionRequest;
import id.ac.ui.cs.advprog.warnetservice.exceptions.NonPositiveParameterException;
import id.ac.ui.cs.advprog.warnetservice.service.pricing.validitycheckhandler.QuantityValidationHandler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;

class QuantityValidationHandlerTest {

    private QuantityValidationHandler handler;

    @BeforeEach
    void setUp(){
        handler = new QuantityValidationHandler();
    }

    @Test
    void testHandleRequestWithPositiveQuantity() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setQuantity(5);

        assertDoesNotThrow(() -> {
            handler.handleRequest(request);
        });
    }

    @Test
    void testHandleRequestWithZeroQuantity() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setQuantity(0);

        assertThrows(NonPositiveParameterException.class, () -> handler.handleRequest(request));
    }

    @Test
    void testHandleRequestWithNegativeQuantity() {
        QuantityValidationHandler handler = new QuantityValidationHandler();
        CreateSessionRequest request = new CreateSessionRequest();
        request.setQuantity(-5);

        assertThrows(NonPositiveParameterException.class, () -> handler.handleRequest(request));
    }
}
