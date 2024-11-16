package com.Final.Project.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler{



    @ExceptionHandler(ProjectIllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgumentException(ProjectIllegalArgumentException ex, WebRequest webRequest) {
        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                ex.getStatus().value(),
                ex.getMessage(),
                webRequest.getDescription(false)
        );

        System.out.println("this is inside exception handler "+ex.getStatus());

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGlobalException(Exception ex, WebRequest webRequest){

        System.out.println("Caught exception: " + ex.getClass().getName() + " - " + ex.getMessage());

        ExceptionResponse response = new ExceptionResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "An error occurred",
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
