package com.freshmart.backend.users.service.impl;


import com.freshmart.backend.auth.repository.AuthRedisRepository;
import com.freshmart.backend.email.service.EmailService;
import com.freshmart.backend.users.dto.*;
import com.freshmart.backend.users.entity.User;
import com.freshmart.backend.users.repository.UserRepository;
import com.freshmart.backend.users.service.UserService;
import com.freshmart.backend.exception.DataNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final AuthRedisRepository authRedisRepository;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, AuthRedisRepository authRedisRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authRedisRepository = authRedisRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }


    @Override
    public UserDto createUser(UserDto userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Override
    public Optional<UserDto> updateUser(Long id, UserDto userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    User updatedUser = convertToEntity(userDTO);
                    updatedUser.setId(existingUser.getId());
                    return convertToDTO(userRepository.save(updatedUser));
                });
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }


    @Transactional
    @Override
    public User register(UserRegisterRequestDto user) {
        Optional<User> userData = userRepository.findByEmail(user.getEmail());
        if(userData.isPresent()){
            throw new DataNotFoundException("Email has already been registered");
        }

        User newUser = user.toEntity();
        userRepository.save(newUser);
        String tokenValue = UUID.randomUUID().toString();
        authRedisRepository.saveVerificationLink(user.getEmail(),tokenValue);
        String htmlBody = "<html>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #F5FAF3; text-align: left;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px;'>" +

                "<div style='text-align: left; background-color: #F5FAF3; padding: 20px;'>" +
                "<img src='https://via.placeholder.com/50' alt='FreshMart' style='width: 50px;'>" +
                "</div>" +

                "<div style='text-align: left; padding: 40px 20px;'>" +
                "<h2 style='color: #666666;'>Verify Your E-mail Address</h2>" +
                "<p style='color: #333;'>Please click on the button below to verify your email address</p>" +
                "<a href='http://localhost:3000/sign-up/confirm?token=" + tokenValue +"&email=" + user.getEmail() + "' style='text-decoration: none;'>" +
                "<button style='background-color: #F5FAF3; color: black; padding: 15px 30px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>VERIFY EMAIL</button>" +
                "</a>" +
                "</div>" +

                "<div style='background-color: #e0e0e0; padding: 8px; text-align: left;'>" +
                "<p style='color: #999;'>Copyrights © Freshmart All Rights Reserved</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";

        emailService.sendEmail(user.getEmail(), "Complete Registration for Freshmart!", htmlBody);
        return newUser;
    }

    @Override
    public User registerSocial(UserRegisterSocialRequestDto user) {
        Optional<User> userData = userRepository.findByEmail(user.getEmail());
        if(userData.isPresent()){
            throw new DataNotFoundException("Email has already been registered");
        }

        User newUser = user.toEntity();
        newUser.setVerifiedAt(Instant.now());
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public User setPassword(ManagePasswordDto data) {
        User user = userRepository.findByEmail(data.getEmail()).orElseThrow(() -> new DataNotFoundException("Email not found"));
        if(!data.getConfirmPassword().equals(data.getPassword())){
            throw new InputMismatchException("Password doesn't match");
        }
        if(user.getVerifiedAt() == null){
            user.setVerifiedAt(Instant.now());
        }
        user.setUpdatedAt(Instant.now());
        user.setPassword(passwordEncoder.encode(data.getPassword()));
        userRepository.save(user);
        return user;
    }

    @Override
    public String verifyEmail(VerifyEmailDto data) {
        Optional<User> userOpt = userRepository.findByEmail(data.getEmail());

        if (userOpt.isEmpty()) {
            return "User not found";
        }

        User user = userOpt.get();

        if (user.getVerifiedAt() != null) {
            return "Already verified";
        }

        String existingToken = authRedisRepository.getVerificationLink(data.getEmail());
        boolean isTokenValid = authRedisRepository.isVerificationLinkValid(data.getEmail());

        if (existingToken != null && isTokenValid && existingToken.equals(data.getToken())) {
            user.setVerifiedAt(Instant.now());
            userRepository.save(user);
            authRedisRepository.deleteVerificationLink(data.getEmail());
            return "Email verified successfully";
        }

        return "Invalid or expired token";
    }

    @Override
    public void newVerificationLink(String email) {
        if(authRedisRepository.isVerificationLinkValid(email)){
            authRedisRepository.deleteVerificationLink(email);
        }
        String tokenValue = UUID.randomUUID().toString();
        authRedisRepository.saveVerificationLink(email,tokenValue);
        String htmlBody = "<html>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #F5FAF3; text-align: left;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px;'>" +

                "<div style='text-align: left; background-color: #F5FAF3; padding: 20px;'>" +
                "<img src='https://via.placeholder.com/50' alt='FreshMart' style='width: 50px;'>" +
                "</div>" +

                "<div style='text-align: left; padding: 40px 20px;'>" +
                "<h2 style='color: #666666;'>Verify Your E-mail Address</h2>" +
                "<p style='color: #333;'>Please click on the button below to verify your email address</p>" +
                "<a href='http://localhost:3000/sign-up/confirm?token=" + tokenValue +"&email=" + email + "' style='text-decoration: none;'>" +
                "<button style='background-color: #F5FAF3; color: black; padding: 15px 30px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>VERIFY EMAIL</button>" +
                "</a>" +
                "</div>" +

                "<div style='background-color: #e0e0e0; padding: 8px; text-align: left;'>" +
                "<p style='color: #999;'>Copyrights © Freshmart All Rights Reserved</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(email, "Complete Registration for Freshmart!", htmlBody);
    }

    @Override
    public void newResetPasswordLink(String email) {
        if(authRedisRepository.isResetPasswordLinkValid(email)){
            authRedisRepository.deleteResetPasswordLink(email);
        }
        String tokenValue = UUID.randomUUID().toString();
        authRedisRepository.saveResetPasswordLink(email,tokenValue);
        String htmlBody = "<html>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #F5FAF3; text-align: left;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px;'>" +

                "<div style='text-align: left; background-color: #F5FAF3; padding: 20px;'>" +
                "<img src='https://via.placeholder.com/50' alt='FreshMart' style='width: 50px;'>" +
                "</div>" +

                "<div style='text-align: left; padding: 40px 20px;'>" +
                "<h1 style='color: #666666;'>Reset Password!</h1>" +
                "<p style='color: #333;'>Click the button below to reset your password:</p>" +
                "<a href='http://localhost:3000//sign-in/forgot-password/confirm-password?token=" + tokenValue +"&email=" + email + "' style='text-decoration: none;'>" +
                "<button style='background-color: #C7253E; color: white; padding: 15px 30px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>RESET PASSWORD</button>" +
                "</a>" +
                "</div>" +

                "<div style='background-color: #e0e0e0; padding: 8px; text-align: left;'>" +
                "<p style='color: #999;'>Copyrights © Freshmart All Rights Reserved</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(email, "Reset password for Freshmart account!", htmlBody);
    }

    @Override
    public String sendResetPasswordLink(String email) {
        Optional<User> userData = userRepository.findByEmail(email);
        if(userData.get().getVerifiedAt() == null){
            return "Not Verified";
        }
        if(userData.get().getPassword() == null){
            return "Not Registered";
        }
        if (authRedisRepository.isResetPasswordLinkValid(email)) {
            authRedisRepository.deleteResetPasswordLink(email);
        }
        String tokenValue = UUID.randomUUID().toString();
        authRedisRepository.saveResetPasswordLink(email,tokenValue);
        String htmlBody = "<html>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #F5FAF3; text-align: left;'>" +
                "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px;'>" +

                "<div style='text-align: left; background-color: #F5FAF3; padding: 20px;'>" +
                "<img src='https://via.placeholder.com/50' alt='FreshMart' style='width: 50px;'>" +
                "</div>" +

                "<div style='text-align: center; padding: 40px 20px;'>" +
                "<h1 style='color: #666666;'>Reset Password!</h1>" +
                "<p style='color: #333;'>Click the button below to reset your password</p>" +
                "<a href='http://localhost:3000//sign-in/forgot-password/confirm-password?token=" + tokenValue +"&email=" + email + "' style='text-decoration: none;'>" +
                "<button style='background-color: #C7253E; color: white; padding: 15px 30px; border: none; border-radius: 5px; font-size: 16px; cursor: pointer;'>RESET PASSWORD</button>" +
                "</a>" +
                "</div>" +

                "<div style='background-color: #e0e0e0; padding: 8px; text-align: left;'>" +
                "<p style='color: #999;'>Copyrights © Freshmart All Rights Reserved</p>" +
                "</div>" +
                "</div>" +
                "</body>" +
                "</html>";
        emailService.sendEmail(email, "Reset password for Freshmart account!", htmlBody);
        return "Success";
    }

    @Override
    public Boolean checkResetPasswordLinkIsValid(CheckResetPasswordLinkDto data) {
        Optional<User> user = userRepository.findByEmail(data.getEmail());
        var existingToken = authRedisRepository.getResetPasswordLink(data.getEmail());
        return user.get().getVerifiedAt() != null && authRedisRepository.isResetPasswordLinkValid(data.getEmail()) && existingToken.equals(data.getToken());
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private UserDto convertToDTO(User user) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    private User convertToEntity(UserDto dto) {
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        return user;
    }
}