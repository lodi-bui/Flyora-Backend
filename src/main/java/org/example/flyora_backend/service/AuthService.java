package org.example.flyora_backend.service;

import java.util.Optional;

import org.example.flyora_backend.DTOs.LoginDTO;
import org.example.flyora_backend.DTOs.LoginResponseDTO;
import org.example.flyora_backend.DTOs.RegisterDTO;
import org.example.flyora_backend.DTOs.RegisterResponseDTO;
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

    public RegisterResponseDTO register(RegisterDTO request) {
        if (accountRepo.findByUsername(request.getUsername()).isPresent()) {
            return new RegisterResponseDTO("Username already exists", null);
        }

        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Invalid role"));

        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword()); // nên mã hóa khi production
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setActive(true);
        account.setApproved(false); // đăng ký xong chưa duyệt
        account.setRole(role);

        accountRepo.save(account);

        return new RegisterResponseDTO("Registered successfully", account.getUsername());
    }


    public LoginResponseDTO login(LoginDTO request) {
        Optional<Account> accountOpt = accountRepo.findByUsername(request.getUsername());

        if (accountOpt.isEmpty()) {
            return new LoginResponseDTO("User not found", null, null);
        }

        Account account = accountOpt.get();
        if (!account.getPassword().equals(request.getPassword())) {
            return new LoginResponseDTO("Invalid credentials", null, null);
        }

        if (!account.isActive() || !account.isApproved()) {
            return new LoginResponseDTO("Account is inactive or not approved yet", null, null);
        }

        return new LoginResponseDTO("Login successful", account.getUsername(), account.getRole().getName());
    }
}
