package org.example.flyora_backend.repository;

import java.util.List;
import org.example.flyora_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);
}
