// src/main/java/org/example/flyora_backend/repository/OrderItemRepository.java
package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.ProductCategory;
import org.example.flyora_backend.model.OrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    @Query("""
        SELECT oi.product.id as productId, SUM(oi.quantity) as totalQuantity
        FROM OrderItem oi
        WHERE oi.product.category = :category
        GROUP BY oi.product.id
        ORDER BY totalQuantity DESC
    """)
    List<Map<String, Object>> findTopSellingProductIdsByCategory(@Param("category") ProductCategory category);
}
