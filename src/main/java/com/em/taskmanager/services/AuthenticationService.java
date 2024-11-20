package com.em.taskmanager.services;

import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = new User()
                .setUsername(request.getUsername())
                .setEmail(request.getEmail())
                .setPasswordHash(passwordEncoder.encode(request.getPassword()))
                .setRoles(Set.of(roleService.getUserRole()));

        userService.create(user);

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
