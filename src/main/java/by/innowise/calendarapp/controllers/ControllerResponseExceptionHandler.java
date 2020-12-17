package by.innowise.calendarapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ControllerResponseExceptionHandler extends ResponseEntityExceptionHandler {

    private final String USER_NOT_AUTHENTICATED = "user not authenticated";

    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<?> handleExceptionUserNameNotFound(AuthenticationException e) {
        return ResponseEntity.status(401)
                .body(USER_NOT_AUTHENTICATED);
    }
}
