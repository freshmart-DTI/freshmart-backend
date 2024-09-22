package com.freshmart.backend.auth.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String email;
    private String role;
}
