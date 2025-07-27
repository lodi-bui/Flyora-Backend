package org.example.flyora_backend.repository;

import java.util.List;
import java.util.Optional;

import org.example.flyora_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerIdOrderByCreatedAtDesc(Integer customerId);

    @Query("SELECT MAX(o.id) FROM Order o")
    Optional<Integer> findMaxId();

    Optional<Order> findByOrderCode(String orderCode);

}
