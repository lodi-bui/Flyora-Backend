package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayOSService payOSService;

    public String createPayOSPayment(int orderId) {
        // Gọi sang service PayOS để tạo link thanh toán
        return payOSService.createPaymentLink(orderId).toString();
    }

}
