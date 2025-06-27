package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.LoginDTO;
import org.example.flyora_backend.DTOs.RegisterDTO;
import org.example.flyora_backend.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Login & Register", description = "Đăng ký và đăng nhập cho khách hàng")
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    /**
     * ✅ API Đăng ký khách hàng
     * 🔹 POST /api/auth/register
     * 🔸 Nhận: username, password, email, phone, name
     * 🔸 Trả: message + userId
     */
    @PostMapping("/register")
    @Operation(
        summary = "Đăng ký tài khoản khách hàng",
        description = "Nhận: username, password, email, phone, name. Trả về message và userId."
    )
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody RegisterDTO request) {
        return ResponseEntity.ok(authService.registerCustomer(request));
    }

    /**
     * ✅ API Đăng nhập khách hàng
     * 🔹 POST /api/auth/login
     * 🔸 Nhận: username, password
     * 🔸 Trả: userId, name, role, token (có thể null)
     */
    @PostMapping("/login")
    @Operation(
        summary = "Đăng nhập tài khoản khách hàng",
        description = "Nhận: username, password. Trả về: userId, name, role, token."
    )
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody LoginDTO request) {
        return ResponseEntity.ok(authService.loginCustomer(request));
    }
}
