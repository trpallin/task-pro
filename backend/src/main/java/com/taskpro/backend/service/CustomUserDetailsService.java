package com.taskpro.backend.service;

import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findById(Long.parseLong(username))
                .orElseThrow(() -> new RuntimeException("User not found"));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getId().toString())
                .password(user.getPassword())
                .build();
    }
}
