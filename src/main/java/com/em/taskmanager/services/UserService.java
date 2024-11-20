package com.em.taskmanager.services;

import com.em.taskmanager.entities.User;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ErrorResponseException(ErrorStatus.USER_USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ErrorResponseException(ErrorStatus.USER_EMAIL_EXISTS);
        }

        return save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.USER_NOT_FOUND_ERROR));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.USER_NOT_FOUND_ERROR));
    }

    public UserDetailsService userDetailsService() {
        return this::getUserByUsername;
    }


    // Получение имени пользователя из контекста Spring Security
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

}
