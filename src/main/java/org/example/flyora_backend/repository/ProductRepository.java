package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p " +
           "WHERE (:categoryId IS NULL OR p.category.id = :categoryId) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice) " +
           "AND (:birdTypeId IS NULL OR p.birdType.id = :birdTypeId)")
    List<Product> filterProducts(
        @Param("categoryId") Integer categoryId,
        @Param("minPrice") BigDecimal minPrice,
        @Param("maxPrice") BigDecimal maxPrice,
        @Param("birdTypeId") Integer birdTypeId // giả sử tag là bird type
    );
    
    List<Product> findByCategory(ProductCategory category);
    
    List<Product> findByStatus(Boolean status);

    List<Product> findByCategoryOrderBySalesCountDesc(ProductCategory category, Pageable pageable);

}

