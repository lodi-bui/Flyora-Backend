// Modified: ProductService.java
package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;
import org.example.flyora_backend.DTOs.ProductDetailDTO;
import org.example.flyora_backend.model.*;
import org.example.flyora_backend.repository.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final FoodDetailRepository foodDetailRepository;
    private final ToyDetailRepository toyDetailRepository;
    private final FurnitureDetailRepository furnitureDetailRepository;

    public List<Product> getTopBestSellersEachCategory(int topN) {
        List<Product> result = new ArrayList<>();
        List<ProductCategory> categories = productCategoryRepository.findAll();

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

    public ProductDetailDTO getProductDetail(Integer productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        String category = product.getCategory().getName().toLowerCase();

        ProductDetailDTO.ProductDetailDTOBuilder builder = ProductDetailDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory().getName());

        switch (category) {
            case "foods" -> {
                FoodDetail food = foodDetailRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("FoodDetail not found"));
                builder
                        .tagName(food.getFoodType().getName())
                        .material(food.getMaterial())
                        .origin(food.getOrigin())
                        .usageTarget(food.getUsageTarget())
                        .weight(food.getWeight())
                        .imageUrl(food.getImageUrl());
            }
            case "toys" -> {
                ToyDetail toy = toyDetailRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("ToyDetail not found"));
                builder
                        .tagName(toy.getToyType().getName())
                        .material(toy.getMaterial())
                        .origin(toy.getOrigin())
                        .color(toy.getColor())
                        .dimensions(toy.getDimensions())
                        .weight(toy.getWeight())
                        .imageUrl(toy.getImageUrl());
            }
            case "furniture" -> {
                FurnitureDetail fur = furnitureDetailRepository.findByProductId(productId)
                        .orElseThrow(() -> new RuntimeException("FurnitureDetail not found"));
                builder
                        .tagName(fur.getFurnitureType().getName())
                        .material(fur.getMaterial())
                        .origin(fur.getOrigin())
                        .color(fur.getColor())
                        .dimensions(fur.getDimensions())
                        .weight(fur.getWeight())
                        .imageUrl(fur.getImageUrl());
            }
            default -> throw new RuntimeException("Unsupported product category");
        }

        return builder.build();
    }
}
