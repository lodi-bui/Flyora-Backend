package org.example.flyora_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/apiapi/admin")
public class AdminController {
    @GetMapping("/dashboard")
    public String dasboard() {
        return "Welcome Admin!";
    }    
}
