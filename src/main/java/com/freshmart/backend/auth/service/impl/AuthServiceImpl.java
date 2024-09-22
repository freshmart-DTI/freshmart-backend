package com.freshmart.backend.auth.service.impl;


import com.freshmart.backend.auth.dto.LoginResponseDto;
import com.freshmart.backend.auth.dto.LoginSocialRequestDto;
import com.freshmart.backend.auth.dto.LoginSocialResponseDto;
import com.freshmart.backend.auth.repository.AuthRedisRepository;
import com.freshmart.backend.auth.service.AuthService;
import com.freshmart.backend.users.entity.User;
import com.freshmart.backend.users.repository.UserRepository;
import com.freshmart.backend.users.service.UserService;
import com.freshmart.backend.exception.DataNotFoundException;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log
public class AuthServiceImpl implements AuthService {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;

    private final AuthRedisRepository authRedisRepository;
    private final UserService userService;

    public AuthServiceImpl(JwtEncoder jwtEncoder, UserRepository userRepository, AuthRedisRepository authRedisRepository, UserService userService) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.authRedisRepository = authRedisRepository;
        this.userService = userService;
    }

    @Override
    public LoginResponseDto generateToken(Authentication authentication) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        var existingKey = authRedisRepository.getJwtKey(authentication.getName());
        LoginResponseDto response = new LoginResponseDto();
        response.setEmail(authentication.getName());
        response.setRole(scope);
        if(existingKey != null){
            log.info("Token already exists for user: " + authentication.getName());
            response.setAccessToken(existingKey);
            return response;
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        if(authRedisRepository.isKeyBlacklisted(jwt)){
            throw new DataNotFoundException("JWT Token has already been blacklisted");
        }
        authRedisRepository.saveJwtKey(authentication.getName(),jwt);
        response.setAccessToken(jwt);
        return response;
    }

    @Override
    public LoginSocialResponseDto generateSocialToken(LoginSocialRequestDto data) {
        Instant now = Instant.now();
        Optional<User> user = userRepository.findByEmail(data.getEmail());
        var existingKey = authRedisRepository.getJwtKey(data.getEmail());
        LoginSocialResponseDto response = new LoginSocialResponseDto();
        response.setEmail(data.getEmail());
        response.setRole(user.get().getRole().name());
        if(existingKey != null){
            log.info("Token already exists for user: " + data.getEmail());
            response.setAccessToken(existingKey);
            return response;
        }

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.HOURS))
                .subject(data.getEmail())
                .claim("scope", user.get().getRole().name())
                .build();

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        if(authRedisRepository.isKeyBlacklisted(jwt)){
            throw new DataNotFoundException("JWT Token has already been blacklisted");
        }
        authRedisRepository.saveJwtKey(data.getEmail(), jwt);
        response.setAccessToken(jwt);
        return response;
    }
}
