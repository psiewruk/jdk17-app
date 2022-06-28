package com.example.demo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityBeans {

    private static final int BCRYPT_STRENGTH = 4;

    @Bean(name = "bCryptTokenEncoder")
    public PasswordEncoder bCryptTokenEncoder() {
        return new BCryptPasswordEncoder(BCRYPT_STRENGTH);
    }
}
