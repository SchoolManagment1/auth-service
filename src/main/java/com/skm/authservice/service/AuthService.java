package com.skm.authservice.service;



import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.skm.authservice.dto.AuthResponse;
import com.skm.authservice.dto.LoginRequest;
import com.skm.authservice.dto.RegisterRequest;
import com.skm.authservice.entity.User;
import com.skm.authservice.enums.Role;
import com.skm.authservice.exception.ResourceNotFoundException;
import com.skm.authservice.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
        	    .firstName(request.getFirstName())
        	    .lastName(request.getLastName())
        	    .address(request.getAddress())
        	    .phone(request.getPhone())
        	    .email(request.getEmail())
        	    .password(passwordEncoder.encode(request.getPassword()))
        	    .role(request.getRole() != null ? request.getRole() : Role.STUDENT)
        	    .build();

        userRepository.save(user);
        return new AuthResponse(jwtService.generateToken(user), user.getRole().name());
    }

    public AuthResponse login(LoginRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        if(user.isStatus()==true) 
        return new AuthResponse(jwtService.generateToken(user), user.getRole().name());
        throw new RuntimeException("User not active");
    }
}
