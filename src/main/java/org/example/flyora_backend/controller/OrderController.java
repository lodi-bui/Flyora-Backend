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
    @Operation(summary = "[Quy trình Bước 1] Tạo đơn hàng nháp", description = """
            Tạo một đơn hàng mới trong hệ thống với trạng thái "PENDING".
            Đây là bước đầu tiên và bắt buộc trong quy trình đặt hàng.

            🔑 **Quyền truy cập:** Khách hàng đã đăng nhập.

            ✅ **Body yêu cầu (CreateOrderDTO):**
            - `customerId` (integer): ID của khách hàng đang đặt hàng.
            - `items` (array): Danh sách sản phẩm, mỗi sản phẩm gồm `productId` và `quantity`.

            🔁 **Trả về:** `orderId` và `status` ("PENDING") của đơn hàng vừa tạo.
            """)
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDTO dto) {
        accessLogService.logAction(dto.getCustomerId(), "Tạo đơn hàng");
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    @PostMapping("/payments")
    @Operation(summary = "[Quy trình Bước 2] Xác nhận thanh toán & Giao hàng", description = """
            Xác nhận phương thức thanh toán cho một đơn hàng đã tạo ở Bước 1.
            **Đây là API then chốt để kích hoạt việc tự động tạo đơn vận chuyển và lưu vào database.**

            🔑 **Quyền truy cập:** Khách hàng đã đăng nhập.

            ✅ **Body yêu cầu (CreatePaymentDTO):**
            - `orderId` (integer): ID của đơn hàng (lấy từ API Bước 1).
            - `customerId` (integer): ID của khách hàng.
            - `paymentMethodId` (integer): 1 (VNPay) hoặc 2 (COD).
            - `amount` (long, *chỉ cho VNPay*): Tổng số tiền thanh toán.
            - **Các trường địa chỉ (bắt buộc cho COD):** `to_name`, `to_phone`, `to_address`, `to_ward_code`, `to_district_id`.

            🔁 **Hành vi và Kết quả trả về:**
            - **Với VNPay (1):** Trả về một `paymentUrl` để frontend chuyển hướng người dùng. **Việc giao hàng sẽ được kích hoạt ở API callback.**
            - **Với COD (2):** **Hệ thống sẽ tự động gọi GHN để tạo đơn vận chuyển, sau đó lưu mã vận đơn và cập nhật trạng thái đơn hàng thành "Shipping".** Trả về `paymentId` và `orderStatus`.
            """)
    public ResponseEntity<?> createOrRedirectPayment(@RequestBody CreatePaymentDTO dto, HttpServletRequest request) {
        accessLogService.logAction(dto.getCustomerId(), "Thanh toán đơn hàng");

        if (dto.getPaymentMethodId() == 1) { // VNPay
            if (dto.getAmount() == null || dto.getAmount() <= 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Số tiền thanh toán không hợp lệ."));
            }

            String url = paymentService.createPayOSPayment(dto.getOrderId());
            return ResponseEntity.ok(Map.of("paymentUrl", url));

        } else if (dto.getPaymentMethodId() == 2) { // COD
            return ResponseEntity.ok(orderService.createPayment(dto));

        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Phương thức thanh toán không hợp lệ."));
        }
    }

    @GetMapping("/payment/vn-pay-callback")
    @Operation(summary = "[Callback] Xử lý kết quả trả về từ VNPay", description = """
            **API này không dành cho frontend gọi trực tiếp.**
            VNPay sẽ tự động gọi về URL này sau khi người dùng hoàn tất thanh toán.

            - **Logic backend:** Nếu thanh toán thành công (`vnp_ResponseCode` = "00"), backend sẽ thực hiện quy trình tạo đơn vận chuyển với GHN và cập nhật trạng thái đơn hàng (tương tự như khi chọn COD).
            """)
    public ResponseEntity<?> handleVnPayCallback(HttpServletRequest request) {
        Map<String, String> params = VNPayUtil.getVNPayResponseParams(request);
        String vnp_ResponseCode = params.get("vnp_ResponseCode");
        String vnp_TxnRef = params.get("vnp_TxnRef");

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
            Lấy danh sách tất cả các đơn hàng đã đặt của một khách hàng cụ thể, sắp xếp theo thứ tự mới nhất.

            🔑 **Quyền truy cập:** Khách hàng đã đăng nhập.

            - **`customerId`** (param): ID của khách hàng cần xem lịch sử.

            🔁 **Trả về:** Danh sách đơn hàng, mỗi đơn hàng bao gồm thông tin chung và danh sách chi tiết các sản phẩm.
            """)
    public ResponseEntity<List<OrderHistoryDTO>> getMyOrders(@RequestParam Integer customerId) {
        accessLogService.logAction(customerId, "Xem lịch sử đơn hàng");
        return ResponseEntity.ok(orderService.getOrdersByCustomer(customerId));
    }
}