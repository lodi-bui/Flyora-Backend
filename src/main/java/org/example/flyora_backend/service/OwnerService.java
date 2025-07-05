package org.example.flyora_backend.service;

import java.util.List;

import org.example.flyora_backend.DTOs.CreateProductDTO;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.example.flyora_backend.model.Product;

public interface OwnerService {
    List<TopProductDTO> getTopSellingProducts(int accountId);

    public Product createProduct(CreateProductDTO dto, Integer accountId);
}
