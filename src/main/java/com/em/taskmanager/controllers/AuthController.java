package com.em.taskmanager.controllers;

import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public void signUp(@RequestBody @Validated SignUpRequest request) {
        authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Validated SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
