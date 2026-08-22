package com.euphorie.user.service;


import com.euphorie.user.entity.User;
import com.euphorie.user.repository.UserRepository;
import com.euphorie.user.mapper.UserMapper;
import com.euphorie.user.dto.UserResponseDto;
import com.euphorie.user.dto.CreateUserDto;
import com.euphorie.exception.NotFoundException;


// component
import org.springframework.stereotype.Service;
// lib
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.List;
import java.util.Optional;

@Service 
public class UserService {

    // private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;
    private final PasswordEncoder  passwordEncoder;
    private final UserRepository   userRepository;  
    private final UserMapper       userMapper;

    public UserService( UserRepository userRepository, 
                        PasswordEncoder  passwordEncoder,
                        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;

    }


    public List<UserResponseDto>  findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> userMapper.toDto(user))
                .toList();
    }  

    public UserResponseDto  findById(Long id ) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("User Not Found"));

        return userMapper.toDto(user);
    }

    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new NotFoundException("User Not Found"));

        return userMapper.toDto(user);
    }

    public UserResponseDto create(CreateUserDto dto) {

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setHashedPassword(
            passwordEncoder.encode(dto.getPassword())
        );

        return userMapper.toDto(userRepository.save(user));
    }

    public UserResponseDto deleteById(Long id ) {
        UserResponseDto userResponseDto = findById(id);
        userRepository.deleteById(userResponseDto.getId());

        return userResponseDto;
    }

}