package org.example.flyora_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesStaff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "shop_owner_id")
    private ShopOwner shopOwner;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
