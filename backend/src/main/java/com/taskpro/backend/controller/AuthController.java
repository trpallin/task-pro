package com.taskpro.backend.controller;

import com.taskpro.backend.dto.LoginRequest;
import com.taskpro.backend.dto.TokenResponse;
import com.taskpro.backend.dto.SignUpRequest;
import com.taskpro.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    ResponseEntity<Void> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        authService.signUpUser(signUpRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        String accessToken = authService.createAccessToken(loginRequest);
        String refreshToken = authService.createRefreshToken(loginRequest);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
//                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        TokenResponse response = new TokenResponse();
        response.setToken(accessToken);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        String refreshToken = authService.getRefreshTokenFromCookie(request);
        String accessToken = authService.refreshAccessToken(refreshToken);
        TokenResponse response = new TokenResponse();
        response.setToken(accessToken);

        return ResponseEntity.ok(response);
    }
}
