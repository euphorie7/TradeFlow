package com.euphorie.user.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.euphorie.user.dto.CreateUserDto;
import com.euphorie.user.dto.UserResponseDto;
import com.euphorie.user.service.UserService;
// Bean 
@RestController()
@RequestMapping("/users")
public class UserController {
    

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    // preauthorise

    // POST /users
    @PostMapping
    public UserResponseDto create(
            @Valid @RequestBody CreateUserDto dto) {
        return userService.create(dto);
    }

    // GET /users
    @GetMapping
    public List<UserResponseDto> findAll() {
        return userService.findAll();
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public UserResponseDto findById(@PathVariable Long id) {
        return userService.findById(id);
    }

    // DELETE /users/{id}
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }



}