package org.guvi.service;

import org.guvi.config.JwtUtil;
import org.guvi.dto.LogInRequest;
import org.guvi.dto.LogInResponse;
import org.guvi.dto.SignUpRequest;
import org.guvi.dto.SignUpResponse;
import org.guvi.error.DuplicateEmailException;
import org.guvi.error.InvalidCredentialsException;
import org.guvi.model.Users;
import org.guvi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private SignUpRequest signUpRequest;
    private LogInRequest logInRequest;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setName("Ashik");
        signUpRequest.setEmail("ashik@example.com");
        signUpRequest.setPassword("password123");

        logInRequest = new LogInRequest();
        logInRequest.setEmail("ashik@example.com");
        logInRequest.setPassword("password123");
    }

    @Test
    void signUp_ShouldCreateUser_WhenEmailDoesNotExist() {

        when(userRepository.existsByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(false);

        Users savedUser = new Users(
                "1L",
                "Ashik",
                "ashik@example.com",
                true,
                "hashedPassword"
        );

        when(userRepository.save(any(Users.class))).thenReturn(savedUser);

        SignUpResponse response = authService.signUp(signUpRequest);

        assertNotNull(response);
        assertEquals("1L", response.getId());
        assertEquals("Ashik", response.getName());
        assertEquals("ashik@example.com", response.getEmail());
        assertTrue(response.isActive());

        verify(userRepository).save(any(Users.class));
    }

    @Test
    void signUp_ShouldThrowException_WhenEmailAlreadyExists() {

        when(userRepository.existsByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> authService.signUp(signUpRequest)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void logIn_ShouldReturnToken_WhenCredentialsAreValid() {

        String rawPassword = "password123";
        String encodedPassword =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                        .encode(rawPassword);

        Users user = new Users(
                "1L",
                "Ashik",
                "ashik@example.com",
                true,
                encodedPassword
        );

        when(userRepository.findByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(Optional.of(user));

        when(jwtUtil.generateToken("1L"))
                .thenReturn("jwt-token");

        LogInResponse response = authService.logIn(logInRequest);

        assertNotNull(response);
        assertEquals("Login succesful", response.getMessage());
        assertEquals("ashik@example.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());

        verify(jwtUtil).generateToken("1L");
    }

    @Test
    void logIn_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.logIn(logInRequest)
        );

        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void logIn_ShouldThrowException_WhenPasswordIsWrong() {

        String encodedPassword =
                new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
                        .encode("correctPassword");

        Users user = new Users(
                "1L",
                "Ashik",
                "ashik@example.com",
                true,
                encodedPassword
        );

        when(userRepository.findByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.logIn(logInRequest)
        );

        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void signUp_ShouldSaveEmailInLowerCase() {

        signUpRequest.setEmail("ASHIK@EXAMPLE.COM");

        when(userRepository.existsByEmailIgnoreCase("ashik@example.com"))
                .thenReturn(false);

        Users savedUser = new Users(
                "1L",
                "Ashik",
                "ashik@example.com",
                true,
                "hashedPassword"
        );

        when(userRepository.save(any(Users.class)))
                .thenReturn(savedUser);

        authService.signUp(signUpRequest);

        ArgumentCaptor<Users> captor = ArgumentCaptor.forClass(Users.class);
        verify(userRepository).save(captor.capture());

        Users capturedUser = captor.getValue();

        assertEquals("ashik@example.com", capturedUser.getEmail());
    }
}