package org.example.flyora_backend.controller;

import java.util.List;
import java.util.Map;

import org.example.flyora_backend.DTOs.CreateOrderDTO;
import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.DTOs.OrderHistoryDTO;
import org.example.flyora_backend.service.AccessLogService;
import org.example.flyora_backend.service.OrderService;
import org.example.flyora_backend.service.PaymentService;
import org.example.flyora_backend.utils.VNPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.*;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Order & Payment", description = "Các API để tạo đơn hàng và thực hiện thanh toán")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @Autowired
    private AccessLogService accessLogService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/orders")
    @Operation(summary = "Tạo đơn hàng mới", description = """
                ✅ Body (CreateOrderDTO):
                - customerId: ID của khách hàng
                - items: Danh sách sản phẩm muốn đặt (mỗi item gồm productId và quantity)

                🛒 Hệ thống sẽ tạo mới đơn hàng với trạng thái `PENDING`.

                🔁 Trả về: orderId và trạng thái đơn hàng.
            """)
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO dto) {
        accessLogService.logAction(dto.getCustomerId(), "Tạo đơn hàng");
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @PostMapping("/payments")
    @Operation(summary = "Tạo hoặc xác nhận thanh toán đơn hàng", description = """
                ✅ Body (CreatePaymentDTO):
                - orderId: ID của đơn hàng
                - customerId: ID khách hàng thực hiện thanh toán
                - paymentMethodId: ID phương thức thanh toán (1 = VNPay, 2 = COD)
                - amount: Số tiền thanh toán (chỉ dùng cho VNPay)

                💳 Nếu chọn VNPay:
                - Hệ thống tạo URL chuyển hướng đến trang thanh toán.

                🚚 Nếu chọn COD:
                - Ghi nhận thanh toán và lưu trạng thái `PAID` ngay.

                🔁 Trả về:
                - Với VNPay: URL thanh toán
                - Với COD: paymentId và trạng thái thanh toán
            """)
    public ResponseEntity<?> createOrRedirectPayment(@RequestBody CreatePaymentDTO dto, HttpServletRequest request) {
        accessLogService.logAction(dto.getCustomerId(), "Thanh toán đơn hàng");

        if (dto.getPaymentMethodId() == 1) { // VNPay
            if (dto.getAmount() == null || dto.getAmount() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Số tiền thanh toán không hợp lệ."));
            }

            String url = paymentService.createVnPayPayment(request, dto);
            return ResponseEntity.ok(Map.of("paymentUrl", url));

        } else if (dto.getPaymentMethodId() == 2) { // COD
            return ResponseEntity.ok(orderService.createPayment(dto));

        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Phương thức thanh toán không hợp lệ."));
        }
    }

    @GetMapping("/payment/vn-pay-callback")
    @Operation(summary = "Xử lý callback từ VNPay sau khi thanh toán", description = """
                ✅ VNPay sẽ redirect về URL này với các tham số như vnp_TxnRef, vnp_ResponseCode, vnp_Amount, vnp_SecureHash...

                📌 Bạn cần xác minh chữ ký `vnp_SecureHash`, kiểm tra mã giao dịch, cập nhật trạng thái đơn hàng và ghi nhận thanh toán.

                🛠 Nếu hợp lệ: Cập nhật đơn hàng → PAID
            """)
    public ResponseEntity<?> handleVnPayCallback(HttpServletRequest request) {
        Map<String, String> params = VNPayUtil.getVNPayResponseParams(request);
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");

        // TODO: xác minh chữ ký, xác minh txnRef nếu cần

        if ("00".equals(vnp_ResponseCode)) {
            // ✅ Thành công: cập nhật trạng thái đơn hàng + payment
            // Có thể gọi orderService.markOrderAsPaidByTxnRef(vnp_TxnRef);
            return ResponseEntity.ok("Thanh toán thành công. Mã giao dịch: " + vnp_TxnRef);
        } else {
            return ResponseEntity.ok("Thanh toán thất bại hoặc bị hủy.");
        }
    }

    @GetMapping("/my-orders")
    @Operation(summary = "Xem lịch sử đơn hàng của khách hàng", description = """
                ✅ Query param:
                - customerId: ID của khách hàng muốn xem lịch sử

                📦 Trả về danh sách đơn hàng theo thứ tự mới nhất,
                mỗi đơn gồm thông tin đơn hàng + danh sách sản phẩm đã đặt.
            """)
    public ResponseEntity<List<OrderHistoryDTO>> getMyOrders(@RequestParam Integer customerId) {
        accessLogService.logAction(customerId, "Xem lịch sử đơn hàng");
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
}