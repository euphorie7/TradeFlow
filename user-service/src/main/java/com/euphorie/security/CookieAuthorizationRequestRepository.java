package com.euphorie.security;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import jakarta.servlet.http.Cookie;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import org.springframework.stereotype.Component;


// repository ? -> composant qui permet de stocker , modifier ou supprimer des donnees


@Component
public class CookieAuthorizationRequestRepository
        implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    
    // Record interne
    private record OAuth2CookieData(
        String state,
        String authorizationUri,
        String redirectUri,
        String clientId,
        Set<String> scopes,
        Map<String, Object> attributes,
        Map<String, Object> additionalParameters
    ) {}

    private final ObjectMapper objectMapper;
    
    public CookieAuthorizationRequestRepository(ObjectMapper obj) {
        this.objectMapper = obj;
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response) {
        
         if (authorizationRequest == null) {
            return;
        }
        System.out.println("SAVE OAUTH2 APPELÉ");

        OAuth2CookieData data = new OAuth2CookieData(
            authorizationRequest.getState(),
            authorizationRequest.getAuthorizationUri(),
            authorizationRequest.getRedirectUri(),
            authorizationRequest.getClientId(),
            authorizationRequest.getScopes(),
            authorizationRequest.getAttributes(),
            authorizationRequest.getAdditionalParameters()
        );

        try {
            System.out.println("SAVE OAUTH2 APPELÉ");
            // 1. Sérialiser la requête OAuth2
        
            String json = objectMapper.writeValueAsString(data);

            // 2. Protéger les données cryptographiquement

            String value = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(json.getBytes(StandardCharsets.UTF_8));

            // 3. Créer un cookie temporaire

            ResponseCookie cookie = ResponseCookie.from("oauth2_request", value)
                    .httpOnly(true)
                    .secure(false) // localhost HTTP
                    .sameSite("Lax")
                    .path("/")
                    .maxAge(Duration.ofMinutes(5))
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        }
        catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }

    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(
            HttpServletRequest request) {
        
        // 1. Récupérer le cookie
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            if (cookie.getName().equals("oauth2_request")) {

                try {
                    // 2. Décoder Base64
                    byte[] decoded = Base64.getUrlDecoder()
                            .decode(cookie.getValue());

                    String json = new String(
                            decoded,
                            StandardCharsets.UTF_8
                    );

                    // 3. JSON -> Record Java
                    OAuth2CookieData data = objectMapper.readValue(
                            json,
                            OAuth2CookieData.class
                    );

                    // 4. Reconstruire la requête OAuth2
                    return OAuth2AuthorizationRequest
                            .authorizationCode()
                            .authorizationUri(data.authorizationUri())
                            .clientId(data.clientId())
                            .redirectUri(data.redirectUri())
                            .scopes(data.scopes())
                            .state(data.state())
                            .attributes(attrs -> attrs.putAll(data.attributes()))
                            .additionalParameters(params -> params.putAll(data.additionalParameters()))
                            .build();

                } catch (Exception e) {
                    return null;
                }
            }
        }

        return null;
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request,
            HttpServletResponse response) {

        // 1. Récupérer la requête OAuth2
        OAuth2AuthorizationRequest authorizationRequest =
                loadAuthorizationRequest(request);

        // 2. Supprimer le cookie
        ResponseCookie cookie = ResponseCookie
                .from("oauth2_request", "")
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // 3. Retourner la requête à Spring Security
        return authorizationRequest;

            
    }
}