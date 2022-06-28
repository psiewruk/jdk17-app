package com.example.demo.authentication.dto;

import lombok.Builder;

import javax.validation.constraints.NotEmpty;

public record RegisterRequest(
    @NotEmpty String email,
    @NotEmpty String password,
    @NotEmpty String phoneNumber
) {
}
