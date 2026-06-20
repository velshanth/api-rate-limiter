package org.guvi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.guvi.config.JwtAuthFilter;
import org.guvi.config.JwtUtil;
import org.guvi.repo.ApiLogRepository;
import org.guvi.service.AuthService;
import org.guvi.service.RateLimiterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GatewayController.class)
@AutoConfigureMockMvc(addFilters = false)
class GatewayControllerTest {

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
    void serviceA_ShouldReturnExpectedResponse() throws Exception {

        mockMvc.perform(get("/api/gateway/service-a"))
                .andExpect(status().isOk())
                .andExpect(content().string("Response From Service A"));
    }

    @Test
    void serviceB_ShouldReturnExpectedResponse() throws Exception {

        mockMvc.perform(get("/api/gateway/service-b"))
                .andExpect(status().isOk())
                .andExpect(content().string("Response From Service B"));
    }
}