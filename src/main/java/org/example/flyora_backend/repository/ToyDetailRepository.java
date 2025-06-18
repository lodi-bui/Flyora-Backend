package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.ToyDetail;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ToyDetailRepository extends JpaRepository<ToyDetail, Integer> {
    Optional<ToyDetail> findByProductId(Integer productId);
}