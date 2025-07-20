package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;
import org.example.flyora_backend.DTOs.WebhookType;
import org.example.flyora_backend.DTOs.WebhookURL;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayOSServiceImpl implements PayOSService {

    @Override
    public String createPaymentLink(int orderId) {
        // TODO: Thực hiện logic tạo link thanh toán PayOS ở đây
        return "https://payos.vn/payment-link/" + orderId;
    }

    @Override
    public void handlePaymentWebhook(WebhookType webhookData) {
        // TODO: Xử lý webhook từ PayOS ở đây
    }

    @Override
    public String confirmWebhook(WebhookURL body) {
        // TODO: Xác nhận webhook từ PayOS ở đây
        return "Webhook confirmed: " + body.getWebhookUrl();
    }
}