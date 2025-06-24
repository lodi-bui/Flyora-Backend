package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductDetailDTO;
import org.example.flyora_backend.DTOs.ProductFilterDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductListDTO> filterProducts(ProductFilterDTO filter) {
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize());

        return productRepository.filterProducts(
                filter.getName(),
                filter.getCategoryId(),
                filter.getBirdTypeId(),
                filter.getMinPrice(),
                filter.getMaxPrice(),
                pageable
        );
    }

    @Override
    public ProductDetailDTO getProductDetail(Integer id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        String imageUrl = null;
        String categoryName = product.getCategory().getName();

        // Lấy ảnh từ bảng chi tiết tương ứng với loại sản phẩm
        switch (categoryName) {
            case "FOODS" -> {
                imageUrl = product.getFoodDetail() != null ? product.getFoodDetail().getImageUrl() : null;
            }
            case "TOYS" -> {
                imageUrl = product.getToyDetail() != null ? product.getToyDetail().getImageUrl() : null;
            }
            case "FURNITURE" -> {
                imageUrl = product.getFurnitureDetail() != null ? product.getFurnitureDetail().getImageUrl() : null;
            }
        }

        return new ProductDetailDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            categoryName,
            product.getBirdType().getName(),
            imageUrl
        );
    }


    @Override
    public List<ProductBestSellerDTO> getTop1BestSellersPerCategory() {
        return productRepository.findBestSellersTop1PerCategory();
    }
}
