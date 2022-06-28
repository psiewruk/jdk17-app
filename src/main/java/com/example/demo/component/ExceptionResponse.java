package com.example.demo.component;

public record ExceptionResponse(
    Object error,
    ErrorType errorType
) {
    public enum ErrorType {
        USER_ALREADY_EXISTS,
        INVALID_CONFIRMATION_CODE,
    }
}
