package com.em.taskmanager.controllers;

import com.em.taskmanager.dtos.CustomUserDetails;
import com.em.taskmanager.dtos.security.JwtAuthenticationResponse;
import com.em.taskmanager.dtos.security.SignInRequest;
import com.em.taskmanager.dtos.security.SignUpRequest;
import com.em.taskmanager.entities.Role;
import com.em.taskmanager.entities.User;
import com.em.taskmanager.repositories.UserRepository;
import com.em.taskmanager.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.Set;

import static com.em.taskmanager.entities.RoleName.ROLE_USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${SECURITY_KEY}")
    private String securityKey;

    @Test
    @DisplayName("Тест проверяет корректность заполнения данных")
    public void shouldSignUpSuccessfully() throws Exception {
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        SignUpRequest request = new SignUpRequest();
        request.setUsername("testuser");
        request.setEmail("testuser@gmail.com");
        request.setPassword("testuserpass");

        mockMvc.perform(post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(userRepository, times(1)).save(userCaptor.capture());
        assertEquals(request.getUsername(), userCaptor.getValue().getUsername());
    }

    @Test
    public void shouldSignInSuccessfully() throws Exception {
        User user = new User()
                .setId(6L)
                .setRoles(Set.of(new Role().setName(ROLE_USER)))
                .setUsername("testuser")
                .setPasswordHash("$2a$10$GMyekCbSZEn8x1OX39nA0.VDXLO1zLWfugv5Rc6auBkR8O7fJCaca");
        doReturn(Optional.of(user)).when(userRepository).findByUsername(any());

        SignInRequest request = new SignInRequest();
        request.setUsername("testuser");
        request.setPassword("testuser");

        MvcResult mvcResult = mockMvc.perform(post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(
                                request)))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JwtAuthenticationResponse jwtAuthenticationResponse = new ObjectMapper().readValue(response, JwtAuthenticationResponse.class);

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(securityKey)))
                .build()
                .parseSignedClaims(jwtAuthenticationResponse.getToken())
                .getPayload();

        assertEquals(user.getUsername(), claims.getSubject());
        assertEquals(jwtService.extractUserId(jwtAuthenticationResponse.getToken()), user.getId());
        assertTrue(jwtService.isTokenValid(jwtAuthenticationResponse.getToken(), CustomUserDetails.toUserDetails(user)));
    }
}
