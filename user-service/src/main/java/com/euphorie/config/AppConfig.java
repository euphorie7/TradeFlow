package com.euphorie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

// security
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.lang.IllegalStateException;


import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean 
    public JwtEncoder jwtEncoder(@Value("${jwt.secret:}") String jwtSecret) {

        if(jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalStateException("JWT secret is missing");
        }

        SecretKey key = new SecretKeySpec(
            jwtSecret.getBytes(StandardCharsets.UTF_8),
            "HmacSHA256" // etiquette
        );

        return NimbusJwtEncoder
            .withSecretKey(key)
            .algorithm(MacAlgorithm.HS256)
            .build();
    }
}