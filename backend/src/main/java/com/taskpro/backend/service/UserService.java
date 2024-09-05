package com.taskpro.backend.service;

import com.taskpro.backend.entity.User;
import com.taskpro.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();

                return userRepository.findByEmail(username) // or findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
            }
        }

        throw new RuntimeException("Authentication failed. No current user.");
    }
}
