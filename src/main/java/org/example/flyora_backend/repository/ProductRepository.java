package org.example.flyora_backend.repository;

import org.example.flyora_backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {

}
