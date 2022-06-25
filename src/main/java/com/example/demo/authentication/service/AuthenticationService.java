package com.example.demo.authentication.service;

import com.example.demo.authentication.dto.LoginRequest;
import com.example.demo.authentication.dto.LoginResponse;
import com.example.demo.config.jwt.JwtTokenUtil;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    public LoginResponse authenticate(LoginRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (BadCredentialsException e) {
            log.info("Login failed for user: {}, bad credentials", request.getUsername());
            throw e;
        } catch (DisabledException e) {
            log.info("Disabled user: {}, tried to log in", request.getUsername());
            throw e;
        }

        final UserDetails user = userService.loadUserByUsername(request.getUsername());
        final String jwtToken = jwtTokenUtil.generateToken(user);

        return new LoginResponse(jwtToken);
    }
}
