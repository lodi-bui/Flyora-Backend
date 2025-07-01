package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.DTOs.CreateOrderDTO;
import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.DTOs.OrderHistoryDTO;
import org.example.flyora_backend.service.AccessLogService;
import org.example.flyora_backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AccessLogService accessLogService;

    @PostMapping("/orders")
    @Operation(summary = "Tạo đơn hàng", description = "Nhận customerId và danh sách sản phẩm. Trả về orderId và trạng thái.")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO dto) {
        accessLogService.logAction(dto.getCustomerId(), "Tạo đơn hàng");
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @PostMapping("/payments")
    @Operation(summary = "Thanh toán đơn hàng", description = "Xác nhận thanh toán với orderId, customerId, paymentMethodId.")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentDTO dto) {
        accessLogService.logAction(dto.getCustomerId(), "Thanh toán đơn hàng");
        return ResponseEntity.ok(orderService.createPayment(dto));
    }
    
    @GetMapping("/my-orders")
    @Operation(
        summary = "Xem lịch sử đơn hàng",
        description = "Lấy danh sách đơn hàng và chi tiết sản phẩm đã đặt của khách hàng"
    )
    public ResponseEntity<List<OrderHistoryDTO>> getMyOrders(@RequestParam Integer customerId) {
        accessLogService.logAction(customerId, "Xem lịch sử đơn hàng");
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
}