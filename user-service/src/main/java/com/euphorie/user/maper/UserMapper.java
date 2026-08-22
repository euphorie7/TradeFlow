package com.euphorie.user.mapper;


import org.springframework.stereotype.Component;
import com.euphorie.user.dto.UserResponseDto;
import com.euphorie.user.entity.User;

@Component
public class UserMapper {


    public UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .pseudo(String.join(" ", user.getEmail().split("@")[0].split("\\.")))
        .build();
    }

} 