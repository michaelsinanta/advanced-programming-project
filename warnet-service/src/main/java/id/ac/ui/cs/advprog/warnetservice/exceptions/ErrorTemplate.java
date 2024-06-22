package id.ac.ui.cs.advprog.warnetservice.exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ErrorTemplate(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
}
