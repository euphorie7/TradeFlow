package com.euphorie.auth.service;


import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;

import com.euphorie.user.dto.UserResponseDto;


import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;


@Service
public class JwtService {

    private JwtEncoder jwtEncoder;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
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

        return jwtEncoder.encode(JwtEncoderParameters.from(payload))
                            .getTokenValue();
    }
}

