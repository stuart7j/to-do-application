package com.jsserverspac.todoapplication.controller;

import com.jsserverspac.todoapplication.dto.AuthRequest;
import com.jsserverspac.todoapplication.dto.*;
import com.jsserverspac.todoapplication.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest req) {
        try {
            String token = authService.register(req);
            return ResponseEntity.ok(new AuthResponse(token,"","User registered successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse("", e.getMessage(),""));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest req) {
        try {
            String token = authService.login(req);
            return ResponseEntity.ok(new AuthResponse(token,"", "User logged in successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse("",e.getMessage(), ""));
        }
    }
}

