package org.example.flyora_backend.repository;

import java.util.List;
import java.util.Optional;

import org.example.flyora_backend.model.ProductReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends JpaRepository<ProductReview, Integer> {
    List<ProductReview> findByProductId(Integer productId);

    @Query("SELECT MAX(r.id) FROM ProductReview r")
    Optional<Integer> findMaxId();

    void deleteAllByProductId(Integer productId);
}