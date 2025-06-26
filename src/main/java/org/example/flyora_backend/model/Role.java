package org.example.flyora_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Role")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    public Role(Integer id) {
        this.id = id;
        this.name = switch (id) {
            case 1 -> "Admin";
            case 2 -> "ShopOwner";
            case 3 -> "SalesStaff";
            case 4 -> "Customer";
            default -> "UNKNOWN";
        };
    }

}