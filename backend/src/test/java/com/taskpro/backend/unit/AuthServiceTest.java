package com.taskpro.backend.unit;

import com.taskpro.backend.controller.AuthController;
import com.taskpro.backend.dto.SignUpRequest;
import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.UserRepository;
import com.taskpro.backend.service.AuthService;
import com.taskpro.backend.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthService actualAuthService;
    @InjectMocks
    private AuthController authController;

    @Test
    void signUpUser_SavesUserInRepository() {
        SignUpRequest request = SignUpRequest.builder()
                .name("test name")
                .email("testemail@test.com")
                .password("password1234")
                .build();
        User user = User.builder()
                .id(1L)
                .name("test name")
                .email("testemail@test.com")
                .password("password1234")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        User result = actualAuthService.signUpUser(request);

        assertEquals(result, user);
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode(anyString());
    }

    @Test
    void signUpUser_CallAuthService() {
        SignUpRequest request = SignUpRequest.builder()
                .name("test name")
                .email("testemail@test.com")
                .password("password1234")
                .build();
        User user = User.builder()
                .id(1L)
                .name("test name")
                .email("testemail@test.com")
                .password("password1234")
                .createdAt(LocalDateTime.now())
                .build();

        when(authService.signUpUser(any(SignUpRequest.class))).thenReturn(user);

        ResponseEntity<Void> result = authController.signUpUser(request);

        assertEquals(result.getStatusCode(), HttpStatus.NO_CONTENT);
        verify(authService, times(1)).signUpUser(any(SignUpRequest.class));
    }
}
