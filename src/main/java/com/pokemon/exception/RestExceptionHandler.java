package com.pokemon.exception;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    // TODO: Check all exception handlers and ensure they return appropriate status codes/messages

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpError> handleUserNotFound(UserNotFoundException ex) {
        log.debug("User not found: {}", ex.getMessage());

        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpError> handleBadRequest(IllegalArgumentException ex) {
        log.debug("Bad request: {}", ex.getMessage());

        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpError> handleUnexpected(Exception ex) {
        log.error("Unexpected error occurred", ex);

        return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpError> handleIllegalState(IllegalStateException ex) {
        log.error("Illegal state: {}", ex.getMessage(), ex);

        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpError> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining("; "));

        message = message.isBlank() ? "Validation failed" : message;

        log.debug("Validation failed: {}", message);

        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HttpError> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream().map(v -> v.getPropertyPath() + ": " + v.getMessage()).collect(Collectors.joining("; "));

        message = message.isBlank() ? "Constraint violation" : message;

        log.debug("Constraint violation: {}", message);

        return build(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<HttpError> handleConflict(ResourceAlreadyExistsException ex) {
        log.debug("Resource conflict: {}", ex.getMessage());

        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<HttpError> handleBusinessRule(BusinessRuleViolationException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());

        return build(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpError> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        return build(HttpStatus.FORBIDDEN, "Access denied");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpError> handleMalformedJson(HttpMessageNotReadableException ex) {
        log.debug("Malformed JSON: {}", ex.getMessage());

        return build(HttpStatus.BAD_REQUEST, "Malformed JSON request");
    }

    private ResponseEntity<HttpError> build(HttpStatus status, String message) {
        HttpError error = new HttpError(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity.status(status).body(error);
    }
}

