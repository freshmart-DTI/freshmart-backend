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
    @NotBlank(message = "Name is required")
    @NotNull
    private String name;

    @NotBlank(message = "Email is required")
    @Email
    @NotNull
    private String email;

    @NotBlank(message = "Phone number is required")
    @NotNull
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public User toEntity() {
        User user = new User();
        user.setRole(role);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setName(name);
        return user;
    }
}
