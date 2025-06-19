package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.LoginDTO;
import org.example.flyora_backend.DTOs.LoginResponseDTO;
import org.example.flyora_backend.DTOs.RegisterDTO;
import org.example.flyora_backend.DTOs.RegisterResponseDTO;
import org.example.flyora_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterDTO request) {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO request) {
    LoginResponseDTO response = authService.login(request);
    return ResponseEntity.ok(response);
    }

}
