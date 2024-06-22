package id.ac.ui.cs.advprog.warnetservice.exceptions.advice;

import id.ac.ui.cs.advprog.warnetservice.exceptions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Object> buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        ErrorTemplate error = new ErrorTemplate(
                exception.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z")));
        return ResponseEntity.status(httpStatus).body(error);
    }

    @ExceptionHandler(value = { PCDoesNotExistException.class, PricingDoesNotExistException.class,
            SessionDoesNotExistException.class, PCPricingPairDoesNotExistException.class })
    public ResponseEntity<Object> notFound(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = { PCIsBeingUsedException.class, SessionExpiredException.class })
    public ResponseEntity<Object> forbidden(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = { NonPositiveParameterException.class, InvalidDateException.class })
    public ResponseEntity<Object> badRequest(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { FoodSoldOutException.class })
    public ResponseEntity<Object> conflicted(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }

    // Fallback exception handler
    @ExceptionHandler(value = { Exception.class, PCInterruptedException.class })
    public ResponseEntity<Object> exception(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
