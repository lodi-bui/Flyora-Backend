package org.example.flyora_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
public class ShipperController {
    @GetMapping("/deliveries")
    public String viewDeliveries() {
        return "Delivery Tasks";
    }
}
