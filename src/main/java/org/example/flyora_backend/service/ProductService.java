// Modified: ProductService.java
package org.example.flyora_backend.service;

import java.util.List;

import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductDetailDTO;
import org.example.flyora_backend.DTOs.ProductFilterDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<ProductListDTO> filterProducts(ProductFilterDTO filter);

    ProductDetailDTO getProductDetail(Integer id);

    List<ProductBestSellerDTO> getTop1BestSellersPerCategory();
}

