package org.example.flyora_backend.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private Boolean status = true;
//    @JsonIgnoreProperties
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<ProductImage> images;
}

