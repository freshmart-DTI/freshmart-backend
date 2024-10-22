package com.freshmart.backend.users.dto;


import lombok.Data;

@Data
public class VerifyEmailDto {
    private String email;
    private String token;
}
