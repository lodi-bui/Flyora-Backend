package org.example.flyora_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/owner")
public class OwnerController {
    @GetMapping("/dashboard")
    public String dasboard() {
        return "Shop Owner Management";
    }
}
