package com.euphorie.security;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.StandardCharsets;

import com.euphorie.security.OAuth2SuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// security
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.IllegalStateException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.config.http.SessionCreationPolicy;
import com.euphorie.jwt.service.JwtService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;  // object of configuration
import org.springframework.security.web.SecurityFilterChain;

import com.euphorie.security.filters.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter filter) {

        FilterRegistrationBean<JwtAuthenticationFilter> registration =
            new FilterRegistrationBean<>(filter);

        registration.setEnabled(false);
        return registration;
    }
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService) {
        return new JwtAuthenticationFilter(jwtService);
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter,
        CustomAuthenticationEntryPoint authenticationEntryPoint,
        CustomAccessDeniedHandler accessDeniedHandler,
        OAuth2SuccessHandler successHandler,
        CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository 
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(config -> config
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
                )
                .oauth2Login(oauth -> oauth
                    .authorizationEndpoint(endpoint -> endpoint
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository)
                    )
                    .successHandler(successHandler)
                )
                .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(config -> config 
  
                        // // Accessible à tout le monde
                        // .requestMatchers("/auth/**").permitAll()

                        // // Accessible uniquement aux administrateurs
                        // .requestMatchers("/admin/**").hasRole("ADMIN")

                        // // Accessible aux utilisateurs et administrateurs
                        // .requestMatchers("/users/**").hasAnyRole("USER", "ADMIN")

                        // // Nécessite une permission précise
                        // .requestMatchers("/documents/**").hasAuthority("READ_DOCUMENTS")

                        // // Accessible uniquement aux utilisateurs authentifiés
                        // .requestMatchers("/profile/**").authenticated()

                        // // Interdit à tout le monde
                        // .requestMatchers("/private/**").denyAll()
                                                    .requestMatchers("/auth/**",
                                                                     "/oauth2/**",
                                                                     "/login/oauth2/**",
                                                                     "/swagger-ui/**",
                                                                     "/api-docs/**",
                                                                     "/actuator/**"
                                                                    ).permitAll()
                                                    .anyRequest().authenticated()
                )
                .build();
        
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  
    

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}