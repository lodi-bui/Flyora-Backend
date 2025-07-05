package org.example.flyora_backend.service;

import java.util.List;

import org.example.flyora_backend.DTOs.TopProductDTO;

public interface OwnerService {
    List<TopProductDTO> getTopSellingProducts(int accountId);

}
