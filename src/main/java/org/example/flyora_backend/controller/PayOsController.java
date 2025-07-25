package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.WebhookType;
import org.example.flyora_backend.DTOs.WebhookURL;
import org.example.flyora_backend.service.PayOSService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payos")
public class PayOsController {

    private final PayOSService payOSService;

    public PayOsController(PayOSService payOSService) {
        this.payOSService = payOSService;
    }

    @PostMapping("/create-link/{orderId}")
    public ResponseEntity<Object> createPaymentLink(@PathVariable int orderId,
            @RequestParam(required = false) Integer amount) {
        Object paymentLink = payOSService.createPaymentLink(orderId, amount != null ? amount : 0);
        return ResponseEntity.ok(paymentLink);
    }

    @PostMapping("/confirm-webhook")
    public ResponseEntity<String> confirmWebhook(@RequestBody WebhookURL body) {
        String result = payOSService.confirmWebhook(body);
        return ResponseEntity.ok(result);
    }

    @RestController
    @RequestMapping("/api/payos")
    @RequiredArgsConstructor
    public class PayOSWebhookController {

        private final PayOSService payOSService;

        @PostMapping("/webhook")
        public ResponseEntity<String> handleWebhook(@RequestBody WebhookType webhookData) {
            payOSService.handlePaymentWebhook(webhookData);
            return ResponseEntity.ok("Webhook received");
        }
    }

}
