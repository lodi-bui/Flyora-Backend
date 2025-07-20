package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.WebhookType;
import org.example.flyora_backend.DTOs.WebhookURL;

public interface PayOSService {
    String createPaymentLink(int orderId);
    void handlePaymentWebhook(WebhookType webhookData);
    String confirmWebhook(WebhookURL body);
}