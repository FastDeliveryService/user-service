package com.fds.userservice.exceptions;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(NoSuchEntityException.class)
    public ResponseEntity<Object> handleNoSuchEntityException(NoSuchEntityException ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.NOT_FOUND.value(), ex.getMessage());

        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));
        ErrorMessage error = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), message);

        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IdentityProviderException.class)
    public ResponseEntity<Object> handleIdentityProviderException(IdentityProviderException ex) {
        ErrorMessage error = new ErrorMessage(HttpStatus.BAD_GATEWAY.value(), ex.getMessage());

        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(error);
    }


    private record ErrorMessage(
            OffsetDateTime timestamp,
            int statusCode,
            String errorMessage
    ) {
        private ErrorMessage(int statusCode, String errorMessage) {
            this(OffsetDateTime.now(), statusCode, errorMessage);
        }
    }
}
