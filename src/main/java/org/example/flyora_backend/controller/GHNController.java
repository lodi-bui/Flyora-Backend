package org.example.flyora_backend.controller;

import java.util.List;
import java.util.Map;

import org.example.flyora_backend.DTOs.CalculateFeeRequestDTO;
import org.example.flyora_backend.DTOs.DistrictDTO;
import org.example.flyora_backend.DTOs.ProvinceDTO;
import org.example.flyora_backend.DTOs.WardDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.service.GHNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(summary = "Lấy danh sách tỉnh/thành", description = """
            Lấy danh sách các tỉnh/thành từ hệ thống giao hàng GHN.
            Tất cả tài khoản đã duyệt đều có thể gọi API này.
            """)
    public ResponseEntity<List<ProvinceDTO>> getProvinces(@RequestParam Integer requesterId) {
        verifyAccess(requesterId); // Xác thực người gọi
        List<ProvinceDTO> provinces = ghnService.getProvinces();
        return ResponseEntity.ok(provinces);
    }

    @GetMapping("/districts")
    @Operation(summary = "Lấy danh sách quận/huyện theo tỉnh/thành", description = "Cung cấp `provinceId` để lấy danh sách các quận/huyện tương ứng.")
    public ResponseEntity<List<DistrictDTO>> getDistricts(
            @RequestParam Integer requesterId,
            @RequestParam int provinceId) {

        verifyAccess(requesterId); // Xác thực người gọi
        List<DistrictDTO> districts = ghnService.getDistricts(provinceId);
        return ResponseEntity.ok(districts);
    }

    @GetMapping("/wards")
    @Operation(
        summary = "Lấy danh sách phường/xã theo quận/huyện",
        description = "Cung cấp `districtId` để lấy danh sách các phường/xã tương ứng."
    )
    public ResponseEntity<List<WardDTO>> getWards(
            @RequestParam Integer requesterId, 
            @RequestParam int districtId) {
                
        verifyAccess(requesterId); // Xác thực người gọi
        List<WardDTO> wards = ghnService.getWard(districtId);
        return ResponseEntity.ok(wards);
    }

    @PostMapping("/calculate-fee")
    @Operation(
        summary = "Tính phí vận chuyển dự kiến",
        description = "Gửi thông tin địa chỉ nhận và gói hàng để nhận lại chi phí vận chuyển."
    )
    public ResponseEntity<Map<String, Object>> calculateShippingFee(
            @RequestParam Integer requesterId, 
            @RequestBody CalculateFeeRequestDTO feeRequest) { // Dùng @RequestBody để nhận JSON
                
        verifyAccess(requesterId);
        Map<String, Object> feeDetails = ghnService.calculateFee(feeRequest);
        return ResponseEntity.ok(feeDetails);
    }
}