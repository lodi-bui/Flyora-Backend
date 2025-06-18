package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.FurnitureDetail;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FurnitureDetailRepository extends JpaRepository<FurnitureDetail, Integer> {
    Optional<FurnitureDetail> findByProductId(Integer productId);
}
