package com.riya.shopsphere.controller;
import com.riya.shopsphere.entity.User;
import com.riya.shopsphere.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.riya.shopsphere.dto.UserResponseDTO;
import com.riya.shopsphere.dto.LoginResponseDTO;
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponseDTO register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody User user) {
        return userService.loginUser(user.getEmail(), user.getPassword());
    }
}