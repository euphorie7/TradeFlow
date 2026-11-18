package com.euphorie.security;

import java.io.IOException;
import java.time.Duration;
import com.euphorie.user.service.UserService;
import com.euphorie.jwt.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataAccessException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.euphorie.user.dto.UserResponseDto;

import com.euphorie.jwt.service.JwtService;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication // remplie par github avec l'identite github

    ) throws IOException {


        try {

        // 1. Récupérer l'utilisateur GitHub
        OAuth2User githubUser =
                (OAuth2User) authentication.getPrincipal();
     
        String githubId = githubUser.getAttribute("id").toString();
        String githubPseudo = githubUser.getAttribute("login");



        // 2. Retrouver ou créer l'utilisateur TradeFlow
        UserResponseDto user = userService.findOrCreate(
                githubId,
                githubPseudo
        );

        // 3. Générer le JWT TradeFlow
        // Adapte cette ligne à la signature de ton JwtService
        String token = jwtService.generateToken(user);

        ResponseCookie cookie = ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(false) // Uniquement pour localhost en HTTP
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        // Redirection vers Angular
        response.sendRedirect("http://localhost:4200/dashboard");
        }
        catch (DataAccessException  e) {

                // response.sendError(500, "Erreur lors de la création du compte");
                response.sendError(500, e.toString());
                return;
        }
    }
}