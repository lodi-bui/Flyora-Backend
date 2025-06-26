package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.AccountDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.model.Role;
import org.example.flyora_backend.repository.AccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    public Account createAccount(AccountDTO dto) {
        Account acc = new Account();
        acc.setUsername(dto.getUsername());
        acc.setPassword(dto.getPassword());
        acc.setPhone(dto.getPhone());

        Role role = new Role();
        role.setId(dto.getRoleId());
        acc.setRole(role);

        acc.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        acc.setIsApproved(dto.getIsApproved() != null ? dto.getIsApproved() : false);
        
        return accountRepository.save(acc);
    }
}
