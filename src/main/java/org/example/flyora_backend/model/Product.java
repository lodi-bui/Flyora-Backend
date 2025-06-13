package org.example.flyora_backend.model;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String description;

    private String name;

    private BigDecimal price;

    private Integer stockQuantity;

    @Builder.Default
    private Boolean status = true;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    public enum ProductCategory 
    {
        FOOD,
        TOYS,
        FURNITURE
    }

    @ManyToOne
    @JoinColumn(name = "birdType_id")
    private BirdType birdType;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private FoodDetail foodDetail;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ToyDetail toyDetail;

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private FurnitureDetail furnitureDetail;

}

