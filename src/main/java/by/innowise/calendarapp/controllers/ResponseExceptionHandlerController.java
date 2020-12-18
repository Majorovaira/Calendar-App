package by.innowise.calendarapp.controllers;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import liquibase.pro.packaged.N;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandlerController extends ResponseEntityExceptionHandler {

    private final String USER_NOT_AUTHORIZED = "user not authorized";
    private final String USER_NOT_AUTHENTICATED = "user not authenticated";
    private final String NOT_VALID_TOKEN = "your token isn't valid";


    @ExceptionHandler(value = AuthenticationException.class)
    protected ResponseEntity<?> handleExceptionUserNameNotFound() {
        return ResponseEntity.status(401)
                .body(USER_NOT_AUTHORIZED);
    }

    @ExceptionHandler(value = SignatureException.class)
    protected ResponseEntity<?> handleSignatureJwtException() {
        return ResponseEntity.status(403)
                .body(NOT_VALID_TOKEN);
    }


}
