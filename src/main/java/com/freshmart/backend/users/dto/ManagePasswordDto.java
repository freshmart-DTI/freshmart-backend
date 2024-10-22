package com.freshmart.backend.users.dto;

import lombok.Data;

@Data
public class ManagePasswordDto {
    private String email;
    private String password;
    private String confirmPassword;
}
