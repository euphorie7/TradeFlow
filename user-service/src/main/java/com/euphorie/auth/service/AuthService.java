package com.euphorie.auth.service;

import org.springframework.stereotype.Service;

import com.euphorie.user.service.UserService;
import com.euphorie.user.dto.UserResponseDto;
import com.euphorie.user.entity.User;
import com.euphorie.user.mapper.UserMapper;
import com.euphorie.user.dto.CreateUserDto;
import com.euphorie.auth.dto.SignUpDto;

import com.euphorie.auth.dto.SignInDto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;


// adapte ce chemin selon l'endroit où tu as créé l'exception
import com.euphorie.exception.ConflictException;

@Service
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(
            UserService userService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            UserMapper userMapper
    ) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public UserResponseDto signUp(SignUpDto dto) {

        if (userService.existsByEmail(dto.getEmail())) {
            throw new ConflictException(
                "Cet utilisateur est deja inscrit."
            );
        }

        UserResponseDto userResponseDto = userService.create(
            CreateUserDto.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build()
        );

        return userResponseDto;
        // return jwtService.generateToken(userResponseDto);
    }

    public String signIn(SignInDto dto) {

        User user = userService.findByEmail(dto.getEmail())
                    .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));

        if(!passwordEncoder.matches(
            dto.getPassword(),
            user.getHashedPassword()
        )) {

            throw new BadCredentialsException("Identifiants invalides");
        }

        String token = jwtService.generateToken(userMapper.toDto(user));

        return token;
    }
}