package com.ftr.iam.service;

import com.ftr.iam.entities.UsersInfo;
import com.ftr.iam.repositories.UsersInfoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomPasswordEncoder {
    
    private final BCryptPasswordEncoder bcryptEncoder;
    private final UsersInfoRepository usersInfoRepository;
    
    public CustomPasswordEncoder(UsersInfoRepository usersInfoRepository) {
        this.bcryptEncoder = new BCryptPasswordEncoder();
        this.usersInfoRepository = usersInfoRepository;
    }
    
    public String encode(String rawPassword) {
        return bcryptEncoder.encode(rawPassword);
    }
    
    public boolean matches(String rawPassword, String username) {
        UsersInfo userInfo = usersInfoRepository.findByUsername(username)
                .orElse(null);
        
        if (userInfo == null) {
            return false;
        }
        
        return bcryptEncoder.matches(rawPassword, userInfo.getPasswordHash());
    }
} 