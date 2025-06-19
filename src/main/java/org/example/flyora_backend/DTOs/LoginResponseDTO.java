package org.example.flyora_backend.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String message;
    private String username;
    private String role;
}