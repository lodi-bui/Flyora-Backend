package org.example.flyora_backend.controller;

import java.util.List;

import org.example.flyora_backend.DTOs.ProvinceDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/shipping")
@Tag(name = "GHN Shipping Service")
public class GHNController {

    @Autowired
    private GHNService ghnService;
    @Autowired
    private AccountRepository accountRepository;

    private void verifyAccess(Integer requesterId) {
        Account acc = accountRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        if (!acc.getIsActive() || !acc.getIsApproved()) {
            throw new RuntimeException("Tài khoản bị khóa hoặc chưa duyệt");
        }
    }

    @GetMapping("/provinces")
    @Operation(
        summary = "Lấy danh sách tỉnh/thành",
        description = """
            Lấy danh sách các tỉnh/thành từ hệ thống giao hàng GHN.

            📌 `requesterId` là ID của tài khoản gọi request. Tất cả tài khoản đã duyệt đều có thể gọi API này.

            🔁 Trả về: Danh sách ProvinceDTO gồm ProvinceID và ProvinceName.
        """
    )
    public ResponseEntity<?> getProvinces(@RequestParam Integer requesterId) {
        verifyAccess(requesterId);
        List<ProvinceDTO> provinces = ghnService.getProvinces();
        return ResponseEntity.ok(provinces);
    }
}