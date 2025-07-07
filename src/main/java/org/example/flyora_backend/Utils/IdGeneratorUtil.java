package org.example.flyora_backend.utils;

import org.example.flyora_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IdGeneratorUtil {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductReviewRepository productReviewRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FoodDetailRepository foodDetailRepository;
    @Autowired
    private ToyDetailRepository toyDetailRepository;
    @Autowired
    private FurnitureDetailRepository furnitureDetailRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ShopOwnerRepository shopOwnerRepository;
    @Autowired
    private SalesStaffRepository salesStaffRepository;

    IdGeneratorUtil(FoodDetailRepository foodDetailRepository) {
        this.foodDetailRepository = foodDetailRepository;
    }

    public Integer generateProductReviewId() {
        return productReviewRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateAccountId() {
        return accountRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateCustomerId() {
        return customerRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateOrderId() {
        return orderRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateOrderItemId() {
        return orderItemRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generatePaymentId() {
        return paymentRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateToyDetailId() {
        return toyDetailRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateFoodDetailId() {
        return foodDetailRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateFurnitureDetailId() {
        return furnitureDetailRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateProductId() {
        return productRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateAdminId() {
        return adminRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateShopOwnerId() {
        return shopOwnerRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateSalesStaffId() {
        return salesStaffRepository.findMaxId().orElse(0) + 1;
    }
}