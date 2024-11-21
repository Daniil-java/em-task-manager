package com.em.taskmanager.controllers;

import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.services.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    public void shouldSignUpSuccessfully() throws Exception {
        SignUpRequest request = new SignUpRequest();
        request.setUsername("Jon");
        request.setEmail("jondoe@gmail.com");
        request.setPassword("password123");

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).signUp(any(SignUpRequest.class));
    }

    @Test
    public void shouldSignInSuccessfully() throws Exception {
        SignInRequest request = new SignInRequest();
        request.setUsername("Jon");
        request.setPassword("password123");

        JwtAuthenticationResponse response = new JwtAuthenticationResponse("mockedJwtToken");

        when(authenticationService.signIn(any(SignInRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }
}
