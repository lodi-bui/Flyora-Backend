package org.example.flyora_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/v1/sales")
public class StaffController {
    @GetMapping("/orders")
    public String viewSales() {
        return "Sales Dashboard";
    }
}
