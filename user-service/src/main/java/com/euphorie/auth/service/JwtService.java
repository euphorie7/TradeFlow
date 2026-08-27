package com.euphorie.auth.service;


import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;

import com.euphorie.user.dto.UserResponseDto;


import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;

import org.springframework.security.authentication.BadCredentialsException;


@Service
public class JwtService {

    private JwtEncoder jwtEncoder;
    private JwtDecoder jwtDecoder;

    public JwtService(JwtEncoder jwtEncoder,
                      JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }


    public String generateToken(UserResponseDto dto ) {

        Instant now = Instant.now();

        JwtClaimsSet payload = JwtClaimsSet.builder()
                                .subject(dto.getEmail())
                                .claim("id" , dto.getId())
                                .issuedAt(now)
                                .expiresAt(now.plusSeconds(3600))
                                .build();

        //Je veux signer CE JWT avec HS256.
        JwsHeader header = JwsHeader
            .with(MacAlgorithm.HS256)
            .build();

        return jwtEncoder.encode(
            JwtEncoderParameters.from(header, payload)
        ).getTokenValue();
    }


    public Jwt valider(String token) {

          return jwtDecoder.decode(token);
        
    }
}

