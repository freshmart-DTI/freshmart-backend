package com.freshmart.backend.users.dto;


import lombok.Data;

@Data
public class CheckVerificationLinkDto {
    private String email;
    private String token;
}
