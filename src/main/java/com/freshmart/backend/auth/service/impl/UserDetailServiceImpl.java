package com.freshmart.backend.auth.service.impl;


import com.freshmart.backend.auth.entity.UserAuth;
import com.freshmart.backend.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var userData = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        return new UserAuth(userData);
    }
}
