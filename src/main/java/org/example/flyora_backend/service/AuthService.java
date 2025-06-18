package org.example.flyora_backend.service;

import java.util.Optional;

import org.example.flyora_backend.DTOs.LoginDTO;
import org.example.flyora_backend.DTOs.RegisterDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.model.Role;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private RoleRepository roleRepo;

    public String register(RegisterDTO request) {
        if (accountRepo.findByUsername(request.getUsername()).isPresent()) {
            return "Username already exists";
        }
        Role role = roleRepo.findById(request.getRoleId())
            .orElseThrow(() -> new RuntimeException("Invalid role"));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword()); // bạn nên mã hóa!
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setActive(true);
        account.setApproved(false);
        account.setRole(role);
        accountRepo.save(account);

        return "Registered successfully";
    }

    public String login(LoginDTO request) {
        Optional<Account> accountOpt = accountRepo.findByUsername(request.getUsername());

        if (accountOpt.isEmpty()) return "User not found";

        Account account = accountOpt.get();
        if (!account.getPassword().equals(request.getPassword())) {
            return "Invalid credentials";
        }

        if (!account.isActive() || !account.isApproved()) {
            return "Account is inactive or not approved yet";
        }

        return "Login successful";
    }
}
