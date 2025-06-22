// src/main/java/org/example/flyora_backend/repository/OrderItemRepository.java
package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.OrderItem;
import org.springframework.data.jpa.repository.*;
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

}

