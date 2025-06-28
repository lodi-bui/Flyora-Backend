package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.AccountDTO;
import org.example.flyora_backend.model.Account;
import org.example.flyora_backend.model.Admin;
import org.example.flyora_backend.model.Customer;
import org.example.flyora_backend.model.Role;
import org.example.flyora_backend.model.SalesStaff;
import org.example.flyora_backend.model.ShopOwner;
import org.example.flyora_backend.repository.AccountRepository;
import org.example.flyora_backend.repository.AdminRepository;
import org.example.flyora_backend.repository.CustomerRepository;
import org.example.flyora_backend.repository.RoleRepository;
import org.example.flyora_backend.repository.SalesStaffRepository;
import org.example.flyora_backend.repository.ShopOwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final SalesStaffRepository salesStaffRepository;

    @Transactional
    public Account createAccount(AccountDTO dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));

        // 1. Tạo tài khoản Account
        Account acc = new Account();
        acc.setUsername(dto.getUsername());
        acc.setPassword(dto.getPassword());
        acc.setPhone(dto.getPhone());
        acc.setEmail(dto.getEmail());
        acc.setRole(role);
        acc.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        acc.setIsApproved(dto.getIsApproved() != null ? dto.getIsApproved() : false);

        // Set approvedBy nếu có
        if (dto.getApprovedBy() != null) {
            Admin approver = adminRepository.findById(dto.getApprovedBy())
                    .orElseThrow(() -> new RuntimeException("Admin duyệt không tồn tại"));
            acc.setApprovedBy(approver);
        }

        accountRepository.save(acc);

        // 2. Tạo bản ghi trong bảng liên quan tùy theo Role
        switch (role.getName()) {
            case "Admin" -> {
                Admin admin = new Admin();
                admin.setId(acc.getId());
                admin.setName(dto.getName());
                admin.setAccount(acc);
                adminRepository.save(admin);
            }
            case "Customer" -> {
                Customer customer = new Customer();
                customer.setId(acc.getId());
                customer.setName(dto.getName());
                customer.setEmail(acc.getEmail());
                customer.setAccount(acc);
                customerRepository.save(customer);
            }
            case "ShopOwner" -> {
                ShopOwner owner = new ShopOwner();
                owner.setId(acc.getId());
                owner.setName(dto.getName());
                owner.setAccount(acc);
                shopOwnerRepository.save(owner);
            }
            case "SalesStaff" -> {
                SalesStaff staff = new SalesStaff();
                staff.setId(acc.getId());
                staff.setName(dto.getName());
                staff.setAccount(acc);
                salesStaffRepository.save(staff);
            }
            default -> throw new RuntimeException("Role không hợp lệ để tạo người dùng cụ thể");
        }
        return acc;
    }
}