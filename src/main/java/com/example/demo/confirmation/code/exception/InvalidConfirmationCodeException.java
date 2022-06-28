package com.example.demo.confirmation.code.exception;

import com.example.demo.component.ServiceException;

import static com.example.demo.component.ExceptionResponse.ErrorType.INVALID_CONFIRMATION_CODE;

public class InvalidConfirmationCodeException extends ServiceException {

    public InvalidConfirmationCodeException() {
        super(INVALID_CONFIRMATION_CODE, "Provided confirmation code is invalid or expired");
    }
}
