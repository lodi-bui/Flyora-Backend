package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.DTOs.ProductBestSellerDTO;
import org.example.flyora_backend.DTOs.ProductDetailDTO;
import org.example.flyora_backend.DTOs.ProductFilterDTO;
import org.example.flyora_backend.DTOs.ProductListDTO;
import org.example.flyora_backend.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product List & Filter", description = "Xem và lọc danh sách sản phẩm")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/filter")
    @Operation(
        summary = "Lọc danh sách sản phẩm",
        description = """
        Nhận: name (keyword), categoryId, birdTypeId, minPrice, maxPrice, page, size.
        Trả về danh sách sản phẩm phân trang: content[], totalElements, totalPages, page number, size.
        """
    )
    public ResponseEntity<?> filterProducts(@RequestBody ProductFilterDTO request) {
        try {
            return ResponseEntity.ok(productService.filterProducts(request));
        } catch (Exception e) {
            e.printStackTrace(); // In chi tiết ra log server
            return ResponseEntity.status(500).body("Lỗi xử lý filter: " + e.getMessage());
        }
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Lấy chi tiết sản phẩm theo ID",
        description = "Trả về thông tin chi tiết của sản phẩm bao gồm tên, mô tả, giá, tồn kho, loại danh mục và loại chim."
    )
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    @GetMapping("/best-sellers/top1")
    @Operation(
        summary = "Lấy sản phẩm bán chạy nhất mỗi danh mục",
        description = "Trả về 1 sản phẩm có số lượng bán cao nhất của từng danh mục."
    )
    public ResponseEntity<List<ProductBestSellerDTO>> getTop1BestSellersPerCategory() {
        return ResponseEntity.ok(productService.getTop1BestSellersPerCategory());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductListDTO>> searchProductsByName(@RequestParam String name) {
        List<ProductListDTO> products = productService.searchByName(name);
        return ResponseEntity.ok(products);
    }

}
