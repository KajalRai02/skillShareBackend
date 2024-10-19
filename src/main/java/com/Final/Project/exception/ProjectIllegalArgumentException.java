package com.Final.Project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ProjectIllegalArgumentException extends RuntimeException {

    private final HttpStatus status;
    public ProjectIllegalArgumentException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }



}

