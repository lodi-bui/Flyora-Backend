package org.example.flyora_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.flyora_backend.model.Product;

@Getter
@Setter
@Entity
public class Inventory {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "quantity")
    private Integer quantity;

}