package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByAccountId(Integer accountId);
}
