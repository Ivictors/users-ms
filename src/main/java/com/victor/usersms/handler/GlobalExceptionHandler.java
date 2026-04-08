package com.victor.usersms.handler;

import com.victor.usersms.dto.StandardErrorDto;
import com.victor.usersms.exceptions.BusinessException;
import com.victor.usersms.exceptions.EmailException;
import com.victor.usersms.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<StandardErrorDto> handleBusinessException(BusinessException ex) {
        return buildError(HttpStatus.CONFLICT, "Business Exception", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<StandardErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, "UserNotFoundException", ex.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<StandardErrorDto> handleEmailNotFoundException(EmailException ex) {
        return buildError(HttpStatus.CONFLICT, "EmailException", ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<StandardErrorDto> handlerBadCredentialsException(BadCredentialsException ex) {
        return buildError(HttpStatus.UNAUTHORIZED, "BadCredentialsException", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorDto> handlerGenericException(Exception ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", "An unexpected error occurred");
    }

    private ResponseEntity<StandardErrorDto> buildError(HttpStatus status, String errorTitle, String message) {
        StandardErrorDto err = new StandardErrorDto(
                LocalDateTime.now(),
                status.value(),
                errorTitle,
                message
        );
        return ResponseEntity.status(status).body(err);
    }
}
