package org.example.flyora_backend.repository;

import java.util.List;

import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
    List<ProductListDTO> findAllByStatusTrue();
}
