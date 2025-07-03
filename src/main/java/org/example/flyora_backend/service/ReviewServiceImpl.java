package org.example.flyora_backend.service;

import java.util.List;
import java.util.Map;
import org.example.flyora_backend.DTOs.ProductReviewDTO;
import org.example.flyora_backend.model.Customer;
import org.example.flyora_backend.model.Product;
import org.example.flyora_backend.model.ProductReview;
import org.example.flyora_backend.repository.CustomerRepository;
import org.example.flyora_backend.repository.ProductReviewRepository;
import org.example.flyora_backend.utils.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ProductReviewRepository reviewRepository;

    private final IdGeneratorUtil idGeneratorUtil;

    private final CustomerRepository customerRepository;

    @Override
    public Map<String, Object> submitReview(ProductReviewDTO dto) {
        ProductReview review = new ProductReview();
        review.setId(idGeneratorUtil.generateProductReviewId());

        // Load từ DB để tránh TransientObjectException
        Customer customer = customerRepository.findById(dto.getCustomerId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));
        Product product = new Product();
        product.setId(dto.getProductId()); // Nếu chắc chắn tồn tại và không có ràng buộc thêm

        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setReview(dto.getComment());

        reviewRepository.save(review);

        return Map.of("message", "Đánh giá sản phẩm thành công");
    }


    @Override
    public List<ProductReviewDTO> getReviewsByProduct(Integer productId) {
        List<ProductReview> reviews = reviewRepository.findByProductId(productId);
        return reviews.stream().map(r -> new ProductReviewDTO(
            r.getCustomer().getId(),
            r.getProduct().getId(),
            r.getRating(),
            r.getReview()
        )).toList();
    }


}