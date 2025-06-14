package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(ProductCategory category);
    
    List<Product> findByStatus(Boolean status);

    List<Product> findByCategoryOrderBySalesCountDesc(ProductCategory category, Pageable pageable);

}

