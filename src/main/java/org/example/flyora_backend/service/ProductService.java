package org.example.flyora_backend.service;

import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductCategory;
import org.example.flyora_backend.repository.ProductCategoryRepository;
import org.example.flyora_backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    public List<Product> getTopBestSellersEachCategory(int topN) {
        List<Product> result = new ArrayList<>();
        List<ProductCategory> categories = productCategoryRepository.findAll(); // ✅ dùng repository

        for (ProductCategory category : categories) {
            List<Product> topProducts = productRepository
                .findByCategoryOrderBySalesCountDesc(category, PageRequest.of(0, topN));
            result.addAll(topProducts);
        }

        return result;
    }

    public List<Product> getProductByCategory(ProductCategory category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getOneProduct(int id) {
        return productRepository.findById(id);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProductById(int id) {
        productRepository.deleteById(id);
    }
}
