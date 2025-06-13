package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByCategory(Product.ProductCategory category);

    Object deleteProductById(Integer id);

    List<Product> findByStatus(Boolean status);
}
