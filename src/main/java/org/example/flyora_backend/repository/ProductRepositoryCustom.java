package org.example.flyora_backend.repository;

import java.math.BigDecimal;
import java.util.List;

import org.example.flyora_backend.DTOs.OwnerProductListDTO;
import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

public interface ProductRepositoryCustom {
    Page<ProductListDTO> filterProducts(
        String name,
        Integer categoryId,
        Integer birdTypeId,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Pageable pageable
    );

    List<ProductBestSellerDTO> findBestSellersTop1PerCategory();

    List<ProductListDTO> searchByName(String name);

    List<TopProductDTO> findTopSellingProductsByShopOwner(int shopOwnerId);

    List<OwnerProductListDTO> findAllByShopOwnerIdOrderByIdAsc(int ownerId);

}
