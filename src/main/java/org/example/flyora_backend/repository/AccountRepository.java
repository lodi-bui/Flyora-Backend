package org.example.flyora_backend.repository;

import org.example.flyora_backend.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
