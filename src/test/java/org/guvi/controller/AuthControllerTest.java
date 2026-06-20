package org.guvi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.guvi.config.JwtAuthFilter;
import org.guvi.config.JwtUtil;
import org.guvi.dto.LogInRequest;
import org.guvi.dto.LogInResponse;
import org.guvi.dto.SignUpRequest;
import org.guvi.dto.SignUpResponse;
import org.guvi.repo.ApiLogRepository;
import org.guvi.service.AuthService;
import org.guvi.service.RateLimiterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private ApiLogRepository apiLogRepository;

    @MockBean
    private RateLimiterService rateLimiterService;

    @Test
    void signUp_ShouldReturnCreated() throws Exception {

        SignUpRequest request = new SignUpRequest();
        request.setName("Ashik");
        request.setEmail("ashik@example.com");
        request.setPassword("password123");

        SignUpResponse response =
                new SignUpResponse(
                        "1",
                        "Ashik",
                        "ashik@example.com",
                        true
                );

        when(authService.signUp(any(SignUpRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ashik"))
                .andExpect(jsonPath("$.email").value("ashik@example.com"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void login_ShouldReturnOk() throws Exception {

        LogInRequest request = new LogInRequest();
        request.setEmail("ashik@example.com");
        request.setPassword("password123");

        LogInResponse response =
                new LogInResponse(
                        "Login succesful",
                        "ashik@example.com",
                        "jwt-token"
                );

        when(authService.logIn(any(LogInRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login succesful"))
                .andExpect(jsonPath("$.email")
                        .value("ashik@example.com"))
                .andExpect(jsonPath("$.token")
                        .value("jwt-token"));
    }
}