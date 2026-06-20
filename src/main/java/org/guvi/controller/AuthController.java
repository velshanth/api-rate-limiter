package org.guvi.controller;

import jakarta.validation.Valid;
import org.guvi.dto.LogInRequest;
import org.guvi.dto.LogInResponse;
import org.guvi.dto.SignUpRequest;
import org.guvi.dto.SignUpResponse;
import org.guvi.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest req){
        SignUpResponse response = authService.signUp(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LogInRequest req){
        LogInResponse response = authService.logIn(req);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
