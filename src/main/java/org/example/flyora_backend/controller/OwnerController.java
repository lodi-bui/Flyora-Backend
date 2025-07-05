package org.example.flyora_backend.controller;

import java.util.List;
import java.util.Map;

import org.example.flyora_backend.DTOs.CreateProductDTO;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.service.AccessLogService;
import org.example.flyora_backend.service.OwnerService;
import org.example.flyora_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/owner")
@Tag(name = "Shop Owner & Staff", description = "Các API của chủ Shop và nhân viên")
public class OwnerController {

    @Autowired
    private AccessLogService accessLogService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private OwnerService ownerService;

    @Operation(summary = "Xem danh sách sản phẩm bán chạy theo doanh số", description = """
                ✅ Dành cho Shop Owner:
                - Hệ thống trả về danh sách các sản phẩm thuộc Shop của người dùng,
                được sắp xếp theo tổng số lượng bán ra (`totalSold`) giảm dần.

                ✅ Response mỗi item gồm:
                - productId: ID sản phẩm
                - productName: Tên sản phẩm
                - imageUrl: URL hình ảnh (lấy từ bảng chi tiết)
                - totalSold: Tổng số lượng đã bán
                - price: Giá sản phẩm

                ❌ Nếu không phải ShopOwner: trả về HTTP 403 (FORBIDDEN)

                ❌ Nếu token không hợp lệ: trả về HTTP 401 (UNAUTHORIZED)
            """)
    @GetMapping("/dashboard/products/top-sales")
    public ResponseEntity<List<TopProductDTO>> getTopSellingProducts(@RequestHeader("Authorization") String token) {
        try {
            Account account = jwtUtil.getAccountFromToken(token);

            if (!"ShopOwner".equalsIgnoreCase(account.getRole().getName())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            List<TopProductDTO> topProducts = ownerService.getTopSellingProducts(account.getId());
            accessLogService.logAction(account.getId(), "Viewed dashboard - top-selling products");

            return ResponseEntity.ok(topProducts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(summary = "Thêm sản phẩm mới", description = "Chỉ dành cho Shop Owner. Sản phẩm sẽ tự động gắn với chi tiết tương ứng tùy loại FOODS, TOYS, FURNITURE.")
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(
            @RequestBody CreateProductDTO dto,
            @RequestHeader("Authorization") String token) {
        Account account = jwtUtil.getAccountFromToken(token);
        Integer accountId = account.getId();
        String role = account.getRole().getName();

        if (!"ShopOwner".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only shop owner can add products");
        }

        try {
            Product newProduct = ownerService.createProduct(dto, accountId);
            accessLogService.logAction(accountId, "ShopOwner tạo sản phẩm mới: " + newProduct.getId());
            return ResponseEntity.ok(Map.of(
                    "message", "Thêm sản phẩm thành công",
                    "productId", newProduct.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
