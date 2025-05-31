package org.example.flyora_backend.service;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getOneProduct(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductByStatus() {
        return productRepository.findByStatus(true);
    }

    public Object addProduct(Product product) {
        return productRepository.save(product);
    }

    public Object deleteProductById(int id) {
        return productRepository.deleteProductById(id);
    }


}
