package org.example.flyora_backend.repository;

import java.util.List;
import java.util.Optional;

import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
    List<ProductListDTO> findAllByStatusTrue();

    @Query("SELECT MAX(p.id) FROM Product p")
    Optional<Integer> findMaxId();
}
