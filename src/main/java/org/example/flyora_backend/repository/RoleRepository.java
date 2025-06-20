package org.example.flyora_backend.repository;

import org.example.flyora_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
