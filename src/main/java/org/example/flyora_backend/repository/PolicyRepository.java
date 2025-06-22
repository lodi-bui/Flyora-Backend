package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Integer> {}