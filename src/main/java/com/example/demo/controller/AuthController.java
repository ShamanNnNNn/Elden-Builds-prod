package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest request){
        System.out.println("=== REGISTER CONTROLLER ===");
        System.out.println("Received request for email: " + request.getEmail());
        System.out.println("Password: " + request.getPassword());

        AuthResponse response = authService.register(request);

        System.out.println("Registration successful for: " + request.getEmail());
        System.out.println("Access token generated: " + response.getAccessToken().substring(0, Math.min(20, response.getAccessToken().length())) + "...");

        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request){
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

}
