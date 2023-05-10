package com.example.hh99miniproject8.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    public ResponseEntity<ErrorStatusResponse> handlingException(CustomException e) {
        return ErrorStatusResponse.errorStatus(e.getErrorCode());
    }
}



