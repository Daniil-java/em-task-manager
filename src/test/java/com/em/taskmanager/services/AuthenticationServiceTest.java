package com.em.taskmanager.services;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.RoleName;
import com.em.taskmanager.entities.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void shouldSignUpUser() {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("Jon");
        request.setEmail("jondoe@gmail.com");
        request.setPassword("password123");

        User user = new User();

        when(userService.createUserBySignUpRequest(any(SignUpRequest.class), any(PasswordEncoder.class)))
                .thenReturn(user);

        authenticationService.signUp(request);

        verify(userService, times(1)).createUserBySignUpRequest(any(SignUpRequest.class), any(PasswordEncoder.class));
    }

    @Test
    public void shouldSignInUser() {
        SignInRequest request = new SignInRequest();
        request.setUsername("Jon");
        request.setPassword("password123");

        String token = "mockedJwtToken";

        when(userService.getUserByUsername(anyString())).thenReturn(new User().setRoles(Set.of(new Role().setName(RoleName.ROLE_USER))));
        when(jwtService.generateToken(any(CustomUserDetails.class))).thenReturn(token);

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        assertEquals("mockedJwtToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
