package org.example.flyora_backend.controller;


import java.util.Map;

import org.example.flyora_backend.DTOs.EmailDTO;
import org.example.flyora_backend.DTOs.OTPverifyDTO;
import org.example.flyora_backend.Utils.JwtUtil;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v2/email")
@RequiredArgsConstructor
@Tag(name = "Email", description = "Gửi email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailDTO emailDTO) {
        // Implementation for sending email
        emailService.sendEmail(emailDTO);
        return ResponseEntity.ok("Email gửi thành công");
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody OTPverifyDTO request
    ) {
        String token = authorizationHeader.replace("Bearer ", "");
        String username = jwtUtil.getUsernameFromToken(token);

        boolean isValid = emailService.verifyOtp(username, request.getOtp());

        if (!isValid) { 
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", "OTP không hợp lệ hoặc đã hết hạn"));
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        //Sinh JWT thật (full quyền)
        String fullToken = jwtUtil.generateToken(account);

        return ResponseEntity.ok(Map.of(
                "message", "Xác thực OTP thành công",
                "token", fullToken
        ));
    }


}
