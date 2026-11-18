package com.euphorie.jwt.service;


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

        JwtClaimsSet.Builder claims = JwtClaimsSet.builder()
            .subject(dto.getId().toString())
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(3600));

        if (dto.getEmail() != null) {
            claims.claim("email", dto.getEmail());
        } else if (dto.getGithubPseudo() != null) {
            claims.claim("github_pseudo", dto.getGithubPseudo());
        }

        JwtClaimsSet payload = claims.build();

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

