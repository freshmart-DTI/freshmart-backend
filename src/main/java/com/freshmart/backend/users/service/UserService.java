package com.freshmart.backend.users.service;


import com.freshmart.backend.users.dto.*;
import com.freshmart.backend.users.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<UserDto> getAllUsers();
    Optional<UserDto> getUserById(Long id);
    User getUserByEmail(String email);
    UserDto createUser(UserDto userDTO);
    Optional<UserDto> updateUser(Long id, UserDto userDTO);
    void deleteUser(Long id);

    User register(UserRegisterRequestDto user);
    User registerSocial(UserRegisterSocialRequestDto user);

    User setPassword(ManagePasswordDto data);

    String verifyEmail(VerifyEmailDto data);


    void newVerificationLink(String email);

    void newResetPasswordLink(String email);

    String sendResetPasswordLink(String email);
    Boolean checkResetPasswordLinkIsValid(CheckResetPasswordLinkDto data);

    User getCurrentUser();
}