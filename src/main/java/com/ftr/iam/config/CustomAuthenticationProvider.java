package com.ftr.iam.config;

import com.ftr.iam.service.CustomPasswordEncoder;
import com.ftr.iam.service.LoginService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private static final Logger logger = LogManager.getLogger(CustomAuthenticationProvider.class);

    private final LoginService loginService;
    private final CustomPasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(LoginService loginService, CustomPasswordEncoder passwordEncoder) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.debug("Authenticating user: {}", authentication.getName());
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = loginService.loadUserByUsername(username);

        if (passwordEncoder.matches(password, username)) {
            return new UsernamePasswordAuthenticationToken(
                    userDetails, password, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
} 