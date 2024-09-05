package com.taskpro.backend.service;

import com.taskpro.backend.dto.LoginRequest;
import com.taskpro.backend.dto.SignUpRequest;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.UserRepository;
import com.taskpro.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public User signUpUser(SignUpRequest signUpRequest) {
        userRepository.findByEmail(signUpRequest.getEmail())
                .ifPresent(user -> {
                    throw new RuntimeException("Email already exists.");
                });
        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public String authenticateUser(LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .filter(user -> isPasswordValid(loginRequest, user))
                .map(user -> jwtUtil.generateToken(loginRequest.getEmail()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
    }

    private boolean isPasswordValid(LoginRequest loginRequest, User existingUser) {
        return passwordEncoder.matches(loginRequest.getPassword(), existingUser.getPassword());
    }
}
