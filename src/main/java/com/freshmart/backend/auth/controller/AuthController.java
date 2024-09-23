package com.freshmart.backend.auth.controller;


import com.freshmart.backend.auth.dto.LoginRequestDto;
import com.freshmart.backend.auth.dto.LoginResponseDto;
import com.freshmart.backend.auth.dto.LoginSocialRequestDto;
import com.freshmart.backend.auth.dto.LoginSocialResponseDto;
import com.freshmart.backend.auth.entity.UserAuth;
import com.freshmart.backend.auth.service.AuthService;
import com.freshmart.backend.users.entity.User;
import com.freshmart.backend.users.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Log
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto userLogin) {
        log.info("User login request received for user: " + userLogin.getEmail());
        try {
            Optional<User> userOptional = userRepository.findByEmail(userLogin.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email Not Found", "message", "No account found with this email address"));
            }

            User user = userOptional.get();
            if (!user.getIsVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Email Not Verified", "message", "Your email address has not been verified"));
            }

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            userLogin.getEmail(),
                            userLogin.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserAuth userDetails = (UserAuth) authentication.getPrincipal();
            log.info("Token requested for user :" + userDetails.getUsername() + " with roles: " + userDetails.getAuthorities().toArray()[0]);
            LoginResponseDto resp = authService.generateToken(authentication);
            Cookie cookie = new Cookie("Sid", resp.getAccessToken());
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/; HttpOnly");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resp);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Credentials", "message", "The provided password is incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Authentication Failed", "message", "An error occurred during authentication"));
        }
    }


    @PostMapping("/login-social")
    public ResponseEntity<?> loginSocial(@RequestBody LoginSocialRequestDto userLogin) {
        log.info("User login request received for user: " + userLogin.getEmail());
        try {
            Optional<User> userOptional = userRepository.findByEmail(userLogin.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Email Not Found", "message", "No account found with this email address"));
            }

            User user = userOptional.get();
            if (!user.getIsVerified()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Email Not Verified", "message", "Your email address has not been verified"));
            }
            log.info("Token requested for user :" + userOptional.get().getName() + " with roles: " + userOptional.get().getRole());
            LoginSocialResponseDto resp = authService.generateSocialToken(userLogin);
            Cookie cookie = new Cookie("Sid", resp.getAccessToken());
            cookie.setMaxAge(3600);
            cookie.setPath("/");
            HttpHeaders headers = new HttpHeaders();
            headers.add("Set-Cookie", cookie.getName() + "=" + cookie.getValue() + "; Path=/; HttpOnly");
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Authentication Failed", "message", "An error occurred during authentication"));
        }
    }
}
