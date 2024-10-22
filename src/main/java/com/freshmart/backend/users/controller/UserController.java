package com.freshmart.backend.users.controller;

import com.freshmart.backend.response.Response;
import com.freshmart.backend.users.dto.*;
import com.freshmart.backend.users.entity.User;
import com.freshmart.backend.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<Response<User>> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return Response.success("User registered successfully", userService.register(userRegisterRequestDto));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Response<String>> resetPassword(@RequestParam String email){
        return Response.success("Reset password link status has been fetched successfully",userService.sendResetPasswordLink(email));
    }

    @PostMapping("/set-password")
    public ResponseEntity<Response<User>> managePassword(@RequestBody ManagePasswordDto data){
        return Response.success("User has been verified", userService.setPassword(data));
    }

    @PostMapping("/change-email")
    public ResponseEntity<Response<Object>> sendNewVerificationLink(@RequestParam String email){
        userService.newVerificationLink(email);
        return Response.success("Verification link has been sent");
    }

    @PostMapping("/send-reset-password-link")
    public ResponseEntity<Response<Object>> sendNewResetPasswordLink(@RequestParam String email){
        userService.newResetPasswordLink(email);
        return Response.success("Reset password link has been sent");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> isVerifiedLinkValid(@RequestBody VerifyEmailDto data){
        boolean isVerified = userService.verifyEmail(data);
        if(isVerified) {
            return Response.success("Verification link status has been fetched");
        } else {
            return Response.failed(HttpStatus.BAD_REQUEST.value(),"Invalid verification token");
        }
    }

    @PostMapping("/verify-password-reset")
    public ResponseEntity<Response<Boolean>> isResetPasswordLinkValid(@RequestBody CheckResetPasswordLinkDto data){
        return Response.success("Verification link status has been fetched", userService.checkResetPasswordLinkIsValid(data));
    }
    @GetMapping("/")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
    return Collections.singletonMap("name", principal.getAttribute("name"));
    }
}