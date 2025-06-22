package org.example.flyora_backend.repository;

import java.util.Optional;

import org.example.flyora_backend.model.SalesStaff;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalesStaffRepository extends JpaRepository<SalesStaff, Integer> {
    Optional<SalesStaff> findByAccountId(Integer accountId);
}
