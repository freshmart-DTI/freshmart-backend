package com.freshmart.backend.users.dto;

import com.freshmart.backend.users.entity.Role;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    private String profilePicture;
    private Boolean isVerified;
    private Role role;
    private Integer warehouseId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}