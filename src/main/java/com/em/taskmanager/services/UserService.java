package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.exceptions.ErrorResponseException;
import com.em.taskmanager.exceptions.ErrorStatus;
import com.em.taskmanager.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

    private final UserRepository userRepository;
    private final RoleService roleService;

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ErrorResponseException(ErrorStatus.USER_USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ErrorResponseException(ErrorStatus.USER_EMAIL_EXISTS);
        }

        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.USER_NOT_FOUND_ERROR));
    }

    public CustomUserDetails getUserDetailsByUsername(String username) {
        return CustomUserDetails.toUserDetails(userRepository.findByUsername(username)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.USER_NOT_FOUND_ERROR)));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ErrorResponseException(ErrorStatus.USER_NOT_FOUND_ERROR));
    }

    public UserDetailsService userDetailsService() {
        return this::loadUserByUsername;
    }


    // Получение имени пользователя из контекста Spring Security
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByUsername(username);
    }

    public User createUserBySignUpRequest(SignUpRequest request, PasswordEncoder passwordEncoder) {
        return create(new User()
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPasswordHash(passwordEncoder.encode(request.getPassword()))
                .setRoles(Set.of(roleService.getUserRole())));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserDetailsByUsername(username);
    }
}
