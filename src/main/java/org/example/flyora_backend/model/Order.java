package org.example.flyora_backend.model;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "`order`") // tránh lỗi từ khóa SQL
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private BigDecimal totalAmount;

    private Timestamp orderDate;

    @Builder.Default
    private Boolean status = true;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;
}
