package com.freshmart.backend.auth.service;

import com.freshmart.backend.auth.dto.LoginResponseDto;
import com.freshmart.backend.auth.dto.LoginSocialRequestDto;
import com.freshmart.backend.auth.dto.LoginSocialResponseDto;
import org.springframework.security.core.Authentication;

public interface AuthService {
    LoginResponseDto generateToken(Authentication authentication);

    LoginSocialResponseDto generateSocialToken(LoginSocialRequestDto data);
}
