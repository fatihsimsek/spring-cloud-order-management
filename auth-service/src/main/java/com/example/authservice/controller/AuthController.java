package com.example.authservice.controller;

import com.example.authservice.entity.User;
import com.example.authservice.payload.ApiResponse;
import com.example.authservice.payload.JwtAuthenticationResponse;
import com.example.authservice.payload.LoginRequest;
import com.example.authservice.security.JwtTokenProvider;
import com.example.authservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;
    private JwtTokenProvider tokenProvider;
    private PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtTokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        Optional<User> user = userService.getByEmail(request.getEmail());
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(false, "User not found"));
        }

        if(!passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "Password not matched"));
        }

        String jwt = tokenProvider.generateToken(user.get());
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}
