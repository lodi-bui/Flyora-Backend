package org.example.flyora_backend.controller;

import org.example.flyora_backend.DTOs.IssueReportDTO;
import org.example.flyora_backend.service.IssueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/issues")
@Tag(name = "Issue Report", description = "Phản hồi và báo lỗi đơn hàng")
@RequiredArgsConstructor
public class IssueController {

    private final IssueService issueService;

    /**
     * ✅ API gửi phản hồi lỗi đơn hàng
     * 🔹 POST /api/v1/issues
     * 🔸 Nhận: customerId, orderId, content
     * 🔸 Trả: message xác nhận
     */
    @PostMapping("/submit")
    @Operation(
        summary = "Gửi báo lỗi đơn hàng",
        description = "Gửi phản hồi liên quan đến đơn hàng từ khách hàng. Trả về message."
    )
    public ResponseEntity<?> submitIssue(@RequestBody IssueReportDTO dto) {
        return ResponseEntity.ok(issueService.submitIssue(dto));
    }
}
