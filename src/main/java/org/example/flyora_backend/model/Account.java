package org.example.flyora_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String phone;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_approved")
    private boolean isApproved;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private Admin approvedBy;
}
