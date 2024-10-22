package com.freshmart.backend.users.dto;


import lombok.Data;

@Data
public class CheckResetPasswordLinkDto {
    private String email;
    private String token;
}
