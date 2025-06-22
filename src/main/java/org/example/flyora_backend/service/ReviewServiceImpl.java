package org.example.flyora_backend.service;

import java.util.Map;
import org.example.flyora_backend.DTOs.ProductReviewDTO;
import org.example.flyora_backend.model.Customer;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductReview;
import org.example.flyora_backend.repository.ProductReviewRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ProductReviewRepository reviewRepository;

    @Override
    public Map<String, Object> submitReview(ProductReviewDTO dto) {
        ProductReview review = new ProductReview();

        Customer customer = new Customer();
        customer.setId(dto.getCustomerId());
        review.setCustomer(customer);

        Product product = new Product();
        product.setId(dto.getProductId());
        review.setProduct(product);

        review.setRating(dto.getRating());
        review.setReview(dto.getComment()); // Đúng với tên field trong entity

        reviewRepository.save(review);

        return Map.of("message", "Đánh giá sản phẩm thành công");
    }

}