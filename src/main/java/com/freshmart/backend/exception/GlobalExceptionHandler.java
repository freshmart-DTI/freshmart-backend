package com.freshmart.backend.exception;

import com.freshmart.backend.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.UnknownHostException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Response<String>> handleAllExceptions(Exception ex) {
        log.error(ex.getMessage(), ex.getCause(), ex);

        if(ex.getCause() instanceof UnknownHostException) {
            return Response.failed(HttpStatus.BAD_REQUEST.value(),"Unable to process the request: " + ex.getLocalizedMessage());
        }

        return Response.failed(HttpStatus.BAD_REQUEST.value(), "Unable to process the request: " + ex.getMessage());
    }
}
