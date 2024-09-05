package com.taskpro.backend.controller;

import com.taskpro.backend.dto.LoginRequest;
import com.taskpro.backend.dto.LoginResponse;
import com.taskpro.backend.dto.SignUpRequest;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.service.AuthService;
import com.taskpro.backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    ResponseEntity<String> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        User user = authService.signUpUser(signUpRequest);
        return ResponseEntity.ok(user.getName());
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.authenticateUser(loginRequest);
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setExpiresIn(JwtUtil.getExpirationTime());
        return ResponseEntity.ok(response);
    }
}
