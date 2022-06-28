package com.example.demo.user.exception;

import com.example.demo.component.ExceptionResponse;
import com.example.demo.component.ServiceException;
import org.springframework.http.HttpStatus;

public class DuplicatedEmailException extends ServiceException {

    public DuplicatedEmailException(String email) {
        super(HttpStatus.BAD_REQUEST, ExceptionResponse.ErrorType.USER_ALREADY_EXISTS,
            String.format("Email %s is already taken by another user", email));
    }
}
