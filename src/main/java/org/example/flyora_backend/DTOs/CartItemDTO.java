package org.example.flyora_backend.DTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private Integer productId;
    private String name;
    private String imageUrl;
    private Integer quantity;
}