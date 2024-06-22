package id.ac.ui.cs.advprog.bayarservice.exception.advice;

import id.ac.ui.cs.advprog.bayarservice.exception.*;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankAlreadyExistsException;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.bank.BankNotSelectedException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponAlreadyUsedException;
import id.ac.ui.cs.advprog.bayarservice.exception.coupon.CouponDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvalidPaymentMethodException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceAlreadyPaidException;
import id.ac.ui.cs.advprog.bayarservice.exception.invoice.InvoiceDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.warnet.SessionDoesNotExistException;
import id.ac.ui.cs.advprog.bayarservice.exception.warnet.WarnetServiceServerException;
import id.ac.ui.cs.advprog.bayarservice.util.Response;
import id.ac.ui.cs.advprog.bayarservice.util.ResponseHandler;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String FAILED = "FAILED";

    @ExceptionHandler(value = {
            BillDoesNotExistException.class,
            BankDoesNotExistException.class,
            InvoiceDoesNotExistException.class,
            CouponDoesNotExistException.class,
            SessionDoesNotExistException.class,
            PaymentLogDoesNotExistException.class
    })
    public ResponseEntity<Object> notAvailableHandler(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.NOT_FOUND, FAILED, null)
        );
    }

    @ExceptionHandler(value = {
            MethodArgumentTypeMismatchException.class,
            InvalidPaymentMethodException.class,
            HttpMessageNotReadableException.class,
            CouponAlreadyUsedException.class,
            InvoiceAlreadyPaidException.class,
            BankNotSelectedException.class
    })
    public ResponseEntity<Object> badRequestHandler(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.BAD_REQUEST, FAILED, null)
        );
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> methodNotAllowed(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, FAILED, null)
        );
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseHandler.generateResponse(new Response(
                "validation error", HttpStatus.BAD_REQUEST, FAILED, errors)
        );
    }

    @ExceptionHandler(value = {Exception.class, WarnetServiceServerException.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, FAILED, null)
        );
    }

    @ExceptionHandler(value = {
            BankAlreadyExistsException.class,
            CouponAlreadyExistException.class,
            InvoiceAlreadyExistException.class
    })
    public ResponseEntity<Object> alreadyExistResourceException(Exception exception) {
        return ResponseHandler.generateResponse(new Response(
                exception.getMessage(), HttpStatus.CONFLICT, FAILED, null)
        );
    }
}
