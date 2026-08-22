package com.euphorie.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Builder;



@Data 
@Builder
public class UserResponseDto {

    private Long id ;
    private String email;
    private String pseudo;
}
