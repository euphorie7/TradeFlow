package com.euphorie.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.euphorie.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class CustomAuthenticationEntryPoint // entree au mecanise de reponse sur un echeec
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception
    ) throws IOException {

        // Status HTTP
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Type du body
        response.setContentType("application/json");

        // Objet Java
        ErrorResponse error = new ErrorResponse(
            401,
            "Authentication required"
        );

        // Objet Java -> JSON -> OutputStream HTTP
        objectMapper.writeValue(
            response.getOutputStream(),
            error
        );
    }
}