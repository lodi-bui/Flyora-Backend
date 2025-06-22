package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Integer> {}