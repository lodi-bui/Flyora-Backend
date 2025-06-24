package org.example.flyora_backend.controller;

import java.util.Map;
import org.example.flyora_backend.DTOs.ProductReviewDTO;
import org.example.flyora_backend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@Tag(name = "Product Review", description = "Gửi đánh giá cho sản phẩm")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/submit")
    @Operation(
        summary = "Gửi đánh giá sản phẩm",
        description = "Khách hàng gửi đánh giá và bình luận cho sản phẩm đã mua"
    )
    public ResponseEntity<Map<String, Object>> submitReview(@Valid @RequestBody ProductReviewDTO request) {
        return ResponseEntity.ok(reviewService.submitReview(request));
    }
}