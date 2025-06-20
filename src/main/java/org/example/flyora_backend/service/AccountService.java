package org.example.flyora_backend.service;

import org.example.flyora_backend.entities.Account;
import org.example.flyora_backend.entities.Role;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository _accountRepository;

    @Autowired
    private RoleRepository _roleRepository;
    public List<Role> getAllAccounts() {
        return _roleRepository.findAll();
    }
}
