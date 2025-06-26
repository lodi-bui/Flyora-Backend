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
        acc.setRole(new Role(dto.getRoleId()));
        acc.setIsActive(true);
        acc.setIsApproved(false);
        return accountRepository.save(acc);
    }
}
