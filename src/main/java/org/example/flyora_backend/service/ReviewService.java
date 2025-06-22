package org.example.flyora_backend.service;

import java.util.Map;

import org.example.flyora_backend.DTOs.ProductReviewDTO;

public interface ReviewService {
    Map<String, Object> submitReview(ProductReviewDTO dto);
}
