package com.freshmart.backend.users.dto;


import com.freshmart.backend.users.entity.Role;
import com.freshmart.backend.users.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRegisterRequestDto {
    @NotBlank(message = "Email is required")
    @Email
    @NotNull(message = "Email is required")
    private String email;

    public User toEntity() {
        User user = new User();
        user.setEmail(email);
        return user;
    }
}
