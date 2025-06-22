package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByAccountId(Integer accountId);
}
