package org.example.flyora_backend.repository;

import java.util.Optional;
import org.example.flyora_backend.model.ShopOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopOwnerRepository extends JpaRepository<ShopOwner, Integer> {
    Optional<ShopOwner> findByAccountId(Integer accountId);
}
