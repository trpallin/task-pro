package com.taskpro.backend.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private long expiresIn;
}
