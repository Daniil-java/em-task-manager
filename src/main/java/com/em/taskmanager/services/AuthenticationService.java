package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public void signUp(SignUpRequest request) {
        userService.createUserBySignUpRequest(request, passwordEncoder);
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        CustomUserDetails user = CustomUserDetails.toUserDetails(userService.getUserByUsername(request.getUsername()));

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
