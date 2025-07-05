package org.example.flyora_backend.controller;

import java.util.List;
import java.util.Map;

import org.example.flyora_backend.DTOs.CreateProductDTO;
import org.example.flyora_backend.DTOs.OwnerProductListDTO;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.service.AccessLogService;
import org.example.flyora_backend.service.OwnerService;
import org.example.flyora_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @Operation(summary = "Thêm sản phẩm mới", description = """
                ✅ Dành cho ShopOwner:
                - Thêm sản phẩm mới vào hệ thống, tự động gắn chi tiết theo loại sản phẩm:
                  FOODS, TOYS hoặc FURNITURE.

                ✅ Request:
                - Truyền `CreateProductDTO` bao gồm thông tin sản phẩm & chi tiết.

                📦 Response:
                - message: Thông báo thành công
                - productId: ID sản phẩm vừa tạo

                ❌ Nếu không phải ShopOwner: trả về HTTP 403 (FORBIDDEN)
                ❌ Nếu dữ liệu không hợp lệ: trả về HTTP 400 (BAD REQUEST)
            """)
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

    @Operation(summary = "Lấy tất cả sản phẩm", description = """
                ✅ Dành cho ShopOwner và Staff:
                - Trả về danh sách tất cả sản phẩm thuộc Shop của người dùng.
                - Sắp xếp theo ID tăng dần.
                - Gồm trạng thái hàng hóa: "Còn hàng" hoặc "Hết hàng".

                📦 Mỗi item gồm:
                - id, name, price, stock, status, imageUrl

                ❌ Nếu không phải ShopOwner hoặc Staff: HTTP 403
            """)
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts(@RequestHeader("Authorization") String token) {
        Account account = jwtUtil.getAccountFromToken(token);
        String role = account.getRole().getName();

        if (!role.equals("ShopOwner") && !role.equals("Staff")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Chỉ Shop Owner hoặc Staff mới được phép xem danh sách sản phẩm");
        }

        List<OwnerProductListDTO> products = ownerService.getAllProductsByOwner(account.getId());
        accessLogService.logAction(account.getId(), "Xem danh sách sản phẩm");

        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Chỉnh sửa sản phẩm", description = """
                ✅ Dành cho ShopOwner và Staff:
                - Cập nhật thông tin sản phẩm theo ID.
                - Bao gồm cập nhật cả thông tin chi tiết (FOODS, TOYS, FURNITURE).

                📦 Response:
                - Trả về `Product` sau khi cập nhật.

                ❌ Nếu không có quyền: HTTP 403
                ❌ Nếu dữ liệu không hợp lệ: HTTP 400
            """)
    @PutMapping("/products/{id}")
    public ResponseEntity<?> editProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id,
            @RequestBody CreateProductDTO dto) {
        Account account = jwtUtil.getAccountFromToken(token);
        String role = account.getRole().getName();

        if (!"ShopOwner".equals(role) && !"Staff".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Chỉ ShopOwner hoặc Staff mới được phép chỉnh sửa sản phẩm");
        }

        try {
            Product updated = ownerService.updateProduct(id, dto, account.getId());
            accessLogService.logAction(account.getId(), "Chỉnh sửa sản phẩm: " + id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Xóa sản phẩm", description = """
                ✅ Chỉ dành cho ShopOwner:
                - Xóa sản phẩm theo ID.

                📦 Response:
                - Trả về chuỗi thông báo "Xóa sản phẩm thành công"

                ❌ Nếu không phải ShopOwner: HTTP 403
                ❌ Nếu sản phẩm không tồn tại hoặc không thuộc quyền sở hữu: HTTP 400
            """)
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer id) {
        Account account = jwtUtil.getAccountFromToken(token);
        String role = account.getRole().getName();

        if (!"ShopOwner".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Chỉ ShopOwner mới được phép xóa sản phẩm");
        }

        try {
            ownerService.deleteProduct(id, account.getId());
            accessLogService.logAction(account.getId(), "Xóa sản phẩm: " + id);
            return ResponseEntity.ok("Xóa sản phẩm thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
