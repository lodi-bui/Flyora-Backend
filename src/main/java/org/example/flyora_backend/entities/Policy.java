package org.example.flyora_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Policy {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private Admin updatedBy;

}