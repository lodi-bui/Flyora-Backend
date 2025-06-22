package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
