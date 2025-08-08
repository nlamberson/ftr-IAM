package com.ftr.iam.controller;

import com.ftr.iam.controller.response.AuthResponse;
import com.ftr.iam.controller.response.Response;
import com.ftr.iam.dto.LoginRequest;
import com.ftr.iam.dto.RegisterRequest;
import com.ftr.iam.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/iam/auth")
@RequiredArgsConstructor
public class AuthController extends BaseController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = new AuthResponse();
        response.setData(authService.register(request));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginRequest request) {

        Response response = new Response();
        String token = authService.login(request);

        if (token != null) {
            response.setMessage("Generated token for given user");
        }
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(response);
    }
} 