package com.ftr.iam.service;

import com.ftr.iam.controller.response.AuthResponse;
import com.ftr.iam.dto.AuthDto;
import com.ftr.iam.dto.LoginRequest;
import com.ftr.iam.dto.RegisterRequest;
import com.ftr.iam.entities.Users;
import com.ftr.iam.entities.UsersInfo;
import com.ftr.iam.repositories.UsersInfoRepository;
import com.ftr.iam.repositories.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsersRepository usersRepository;
    private final UsersInfoRepository usersInfoRepository;
    private final CustomPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthDto register(RegisterRequest request) {
        // Check if username already exists
        if (usersRepository.existsByUsername(request.getUsername())) {
            return new AuthDto(null, null, "Username already exists");
        }
        
        // Check if email already exists
        if (usersRepository.existsByEmail(request.getEmail())) {
            return new AuthDto(null, null, "Email already exists");
        }

        // Create new user
        Date now = new Date();
        Users user = new Users();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setCreatedBy("SYSTEM");
        user.setCreatedDate(now);
        user.setUpdatedBy("SYSTEM");
        user.setUpdatedDate(now);

        user = usersRepository.save(user);

        // Create user info with password hash and salt
        UsersInfo userInfo = new UsersInfo();
        userInfo.setUserId(user.getUserId());
        userInfo.setUsername(user.getUsername());
        
        // Generate salt and hash password
        String salt = generateSalt();
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        
        userInfo.setSalt(salt);
        userInfo.setPasswordHash(hashedPassword);
        userInfo.setCreatedBy("SYSTEM");
        userInfo.setCreatedDate(now);
        userInfo.setUpdatedBy("SYSTEM");
        userInfo.setUpdatedDate(now);
        
        // Save both entities
        usersInfoRepository.save(userInfo);

        return new AuthDto(user.getUsername(), user.getEmail(), "User registered successfully");
    }
    
    public String login(LoginRequest request) {
        try {
            // Authenticate user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            // Get user from database
            Users user = usersRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Get user info for password hash
            UsersInfo userInfo = usersInfoRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User info not found"));

            // Generate JWT token
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(userInfo.getPasswordHash())
                    .roles("USER")
                    .build();
            
            String token = null;
            try {
                token = jwtService.generateToken(userDetails);
            } catch (Exception e) {
                throw new RuntimeException("Error generating token, throwing error", e);
            }
            
            return token;
            
        } catch (Exception e) {
            throw new RuntimeException("Invalid Username/Password", e);
        }
    }
    
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
} 