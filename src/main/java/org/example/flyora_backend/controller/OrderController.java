package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.CreateOrderDTO;
import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Order & Payment", description = "Tạo đơn hàng và thanh toán")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * ✅ Tạo đơn hàng mới
     * 🔹 POST /api/v1/orders
     * 🔸 Nhận: customerId + danh sách items (productId, quantity)
     * 🔸 Trả: orderId, status
     */
    @PostMapping("/orders")
    @Operation(summary = "Tạo đơn hàng", description = "Nhận customerId và danh sách sản phẩm. Trả về orderId và trạng thái.")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO dto) {
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    /**
     * ✅ Thanh toán đơn hàng
     * 🔹 POST /api/v1/payments
     * 🔸 Nhận: orderId, customerId, paymentMethodId
     * 🔸 Trả: paymentId, status
     */
    @PostMapping("/payments")
    @Operation(summary = "Thanh toán đơn hàng", description = "Xác nhận thanh toán với orderId, customerId, paymentMethodId.")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO dto) {
        return ResponseEntity.ok(orderService.createPayment(dto));
    }
}