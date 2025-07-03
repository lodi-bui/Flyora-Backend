package org.example.flyora_backend.utils;

import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.repository.CustomerRepository;
import org.example.flyora_backend.repository.ProductReviewRepository;
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

    public Integer generateProductReviewId() {
        return productReviewRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateAccountId() {
        return accountRepository.findMaxId().orElse(0) + 1;
    }

    public Integer generateCustomerId() {
        return customerRepository.findMaxId().orElse(0) + 1;
    }
}