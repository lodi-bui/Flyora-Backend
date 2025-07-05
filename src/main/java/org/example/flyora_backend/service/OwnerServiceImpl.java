package org.example.flyora_backend.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.example.flyora_backend.DTOs.TopProductDTO;
import org.example.flyora_backend.model.ShopOwner;
import org.example.flyora_backend.repository.ProductRepository;
import org.example.flyora_backend.repository.ShopOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OwnerServiceImpl implements OwnerService {
    @Autowired
    private ShopOwnerRepository shopOwnerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<TopProductDTO> getTopSellingProducts(int accountId) {
        Optional<ShopOwner> shopOwnerOpt = shopOwnerRepository.findByAccountId(accountId);
        if (shopOwnerOpt.isEmpty())
            return Collections.emptyList();

        int shopOwnerId = shopOwnerOpt.get().getId();
        return productRepository.findTopSellingProductsByShopOwner(shopOwnerId);
    }

}
