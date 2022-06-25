package com.example.demo.authentication.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
public class LoginRequest {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
