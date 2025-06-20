package org.example.flyora_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.flyora_backend.model.Product;

@Getter
@Setter
@Entity
public class ProductReview {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Lob
    @Column(name = "review")
    private String review;

    @Column(name = "rating")
    private Integer rating;

}