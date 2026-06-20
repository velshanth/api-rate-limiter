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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public SignUpResponse signUp(SignUpRequest req) {
        String name = req.getName();
        String email = req.getEmail().toLowerCase();
        String password = req.getPassword();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new DuplicateEmailException(email);
        }
        String passwordHash = bCryptPasswordEncoder.encode(password);

        Users user = new Users(null, name,email,true,passwordHash);
        Users savedUser =  userRepository.save(user);
        return new SignUpResponse(savedUser.getId(),savedUser.getName(), savedUser.getEmail(),savedUser.getActive());
    }

    public LogInResponse logIn(LogInRequest req){
        String email = req.getEmail().toLowerCase();
        String password = req.getPassword();
        Users user = userRepository.findByEmailIgnoreCase(email).orElseThrow(InvalidCredentialsException::new);
        boolean isPasswordMatch = bCryptPasswordEncoder.matches(password,user.getPasswordHash());
        if(!isPasswordMatch) {
            throw new InvalidCredentialsException();
        }
        String token = jwtUtil.generateToken(user.getId());
        return new LogInResponse(
                "Login succesful",
                user.getEmail(),
                token
        );
    }

}

