package com.example.demo.component;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private final HttpStatus responseStatus;
    private final transient ExceptionResponse responseBody;

    public ServiceException(HttpStatus responseStatus, ExceptionResponse.ErrorType errorType, String message) {
        super(message);
        this.responseStatus = responseStatus;
        this.responseBody = new ExceptionResponse(message, errorType);
    }

    public ServiceException(ExceptionResponse.ErrorType errorType, String message) {
        this(HttpStatus.BAD_REQUEST, errorType, message);
    }
}
