package org.example.flyora_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.flyora_backend.model.Order;

@Getter
@Setter
@Entity
public class IssueReport {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Lob
    @Column(name = "content")
    private String content;

}