package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

}