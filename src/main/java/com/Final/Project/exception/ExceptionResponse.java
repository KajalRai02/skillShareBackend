package com.Final.Project.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timestamp;

    private int statusCode;

    private String message;

    private String details;


}
