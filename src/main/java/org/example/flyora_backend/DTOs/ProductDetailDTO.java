package org.example.flyora_backend.DTOs;

import java.math.BigDecimal;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDTO {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String category;

    // Tag và chi tiết cụ thể
    private String tagName; // Tên loại như foodType, toyType...
    private String material;
    private String origin;
    private String usageTarget;     // FOOD
    private String color;           // TOY, FURNITURE
    private String dimensions;      // TOY, FURNITURE
    private String imageUrl;
    private double weight;
}