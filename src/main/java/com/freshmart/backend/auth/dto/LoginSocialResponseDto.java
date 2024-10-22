package com.freshmart.backend.auth.dto;


import lombok.Data;

@Data
public class LoginSocialResponseDto {
    private String accessToken;
    private String email;
    private String role;
}
