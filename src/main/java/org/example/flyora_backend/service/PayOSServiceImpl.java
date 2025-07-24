package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flyora_backend.DTOs.WebhookType;
import org.example.flyora_backend.DTOs.WebhookURL;
import org.example.flyora_backend.model.Order;
import org.example.flyora_backend.repository.OrderRepository;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;
import vn.payos.type.WebhookData;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PayOSServiceImpl implements PayOSService {

    private final PayOS payOS;
    private final OrderRepository orderRepository;

    @Override
    public Map<String, String> createPaymentLink(int orderId, int amount) {
        // ❌ Đừng tạo orderCode mới ở đây
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        String orderCode = order.getOrderCode(); // ✅ Lấy từ DB

        try {
            PaymentData paymentData = PaymentData.builder()
                    .orderCode(Long.parseLong(orderCode)) // phải là Long hợp lệ
                    .amount(amount)
                    .description(orderCode)
                    .returnUrl("https://localhost:3000")
                    .cancelUrl("http://127.0.0.1:5500/cancel.html")
                    .build();

            CheckoutResponseData response = payOS.createPaymentLink(paymentData);

            log.info("Tạo link thanh toán thành công: {}", response.getCheckoutUrl());

            return Map.of(
                    "paymentUrl", response.getCheckoutUrl(),
                    "orderCode", orderCode);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo link thanh toán: " + e.getMessage(), e);
        }
    }

    @Override
    public void handlePaymentWebhook(WebhookType webhookData) {
        log.info("📩 Nhận webhook PayOS: {}", webhookData);

        WebhookData data = webhookData.getData();

        if (data == null || data.getOrderCode() == 0) {
            log.error("❌ Không tìm thấy orderCode trong dữ liệu webhook.");
            return;
        }

        String orderCode = String.valueOf(data.getOrderCode());
        String statusCode = webhookData.getCode(); // hoặc từ data.getCode() nếu bạn cần
        String statusDesc = webhookData.getDesc();

        if ("00".equals(statusCode) || webhookData.isSuccess()) {
            Order order = orderRepository.findByOrderCode(orderCode)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng với orderCode: " + orderCode));

            if (!"PAID".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("PAID");
                orderRepository.save(order);
                log.info("✅ Đã cập nhật đơn hàng [{}] sang trạng thái PAID", orderCode);
            } else {
                log.info("ℹ️ Đơn hàng [{}] đã ở trạng thái PAID", orderCode);
            }
        } else {
            log.warn("⚠️ Webhook không thành công. Code: {}, Desc: {}", statusCode, statusDesc);

            Order order = orderRepository.findByOrderCode(orderCode)
                    .orElse(null);
            if (order != null && !"PAID".equalsIgnoreCase(order.getStatus())) {
                order.setStatus("CANCELLED");
                orderRepository.save(order);
                log.warn("⚠️ Đã cập nhật trạng thái đơn hàng [{}] thành CANCELLED do thanh toán thất bại", orderCode);
            }
        }

    }

    

    @Override
    public String confirmWebhook(WebhookURL body) {
        log.info("🔔 Webhook xác nhận từ: {}", body.getWebhookUrl());
        return "Webhook confirmed: " + body.getWebhookUrl();
    }
}
