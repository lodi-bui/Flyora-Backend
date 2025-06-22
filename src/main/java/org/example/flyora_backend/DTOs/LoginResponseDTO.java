package org.example.flyora_backend.DTOs;

import lombok.Data;

@Data
public class LoginResponseDTO {
    private Integer userId;
    private String name;
    private String role;
    private String token; // placeholder nếu có JWT (hiện có thể null)
}
