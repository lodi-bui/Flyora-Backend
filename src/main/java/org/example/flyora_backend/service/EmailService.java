package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.EmailDTO;

public interface EmailService {
    void sendEmail(EmailDTO emailDTO);

    void sendOTPEmail(String to, String otp); 

    String createAndStoreOtp(String key);

    boolean verifyOtp(String key, String otp);
}
