package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.WebhookType;
import org.example.flyora_backend.DTOs.WebhookURL;
import org.example.flyora_backend.service.PayOSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payos")
public class PayOsController {

    private final PayOSService payOSService;

    @Autowired
    public PayOsController(PayOSService payOSService) {
        this.payOSService = payOSService;
    }

    @PostMapping("/create-link/{orderId}")
    public ResponseEntity<Object> createPaymentLink(@PathVariable int orderId) {
        Object paymentLink = payOSService.createPaymentLink(orderId);
        return ResponseEntity.ok(paymentLink);
    }

    @PostMapping("/confirm-webhook")
    public ResponseEntity<String> confirmWebhook(@RequestBody WebhookURL body) {
        String result = payOSService.confirmWebhook(body);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody WebhookType webhookData) {
        try {
            payOSService.handlePaymentWebhook(webhookData);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            throw new RuntimeException("Error handling webhook", ex);
        }
    }
}
