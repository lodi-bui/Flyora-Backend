package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    void deleteAllByProductId(Integer productId);
}