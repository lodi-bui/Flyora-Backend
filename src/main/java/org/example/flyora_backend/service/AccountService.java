package org.example.flyora_backend.service;

import lombok.RequiredArgsConstructor;
import org.example.flyora_backend.DTOs.AccountDTO;
import org.example.flyora_backend.DTOs.UserDTO;
import org.example.flyora_backend.model.*;
import org.example.flyora_backend.repository.*;
import org.example.flyora_backend.utils.IdGeneratorUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final AdminRepository adminRepository;
    private final CustomerRepository customerRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final SalesStaffRepository salesStaffRepository;
    private final IdGeneratorUtil idGeneratorUtil;

    @Transactional
    public Account createAccount(AccountDTO dto) {
        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));

        // 1. Tạo tài khoản Account
        Account acc = new Account();
        acc.setId(idGeneratorUtil.generateAccountId());
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

        // 2. Tạo bản ghi cụ thể theo role
        switch (role.getName()) {
            case "Admin" -> {
                Admin admin = new Admin();
                admin.setId(idGeneratorUtil.generateAdminId());
                admin.setName(dto.getName());
                admin.setAccount(acc);
                adminRepository.save(admin);
            }
            case "Customer" -> {
                Customer customer = new Customer();
                customer.setId(idGeneratorUtil.generateCustomerId());
                customer.setName(dto.getName());
                customer.setEmail(acc.getEmail());
                customer.setAccount(acc);
                customerRepository.save(customer);
            }
            case "ShopOwner" -> {
                ShopOwner owner = new ShopOwner();
                owner.setId(idGeneratorUtil.generateShopOwnerId());
                owner.setName(dto.getName());
                owner.setAccount(acc);
                shopOwnerRepository.save(owner);
            }
            case "SalesStaff" -> {
                SalesStaff staff = new SalesStaff();
                staff.setId(idGeneratorUtil.generateSalesStaffId());
                staff.setName(dto.getName());
                staff.setAccount(acc);
                salesStaffRepository.save(staff);
            }
            default -> throw new RuntimeException("Role không hợp lệ để tạo người dùng cụ thể");
        }

        return acc;
    }

    @Transactional
    public List<UserDTO> getAllAccounts() {
        return accountRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Account updateAccount(Integer id, AccountDTO dto) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        acc.setUsername(dto.getUsername());
        acc.setPassword(dto.getPassword());
        acc.setPhone(dto.getPhone());
        acc.setEmail(dto.getEmail());

        if (dto.getRoleId() != null) {
            Role role = roleRepository.findById(dto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy role"));
            acc.setRole(role);
        }

        if (dto.getApprovedBy() != null) {
            Admin admin = adminRepository.findById(dto.getApprovedBy())
                    .orElseThrow(() -> new RuntimeException("Admin duyệt không tồn tại"));
            acc.setApprovedBy(admin);
        }

        acc.setIsActive(dto.getIsActive());
        acc.setIsApproved(dto.getIsApproved());

        return accountRepository.save(acc);
    }

    @Transactional
    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        // Xóa các entity phụ thuộc (Customer, ShopOwner, SalesStaff, Admin)
        if (account.getRole().getName().equalsIgnoreCase("Customer")) {
            customerRepository.deleteByAccountId(id);
        } else if (account.getRole().getName().equalsIgnoreCase("ShopOwner")) {
            shopOwnerRepository.deleteByAccountId(id);
        } else if (account.getRole().getName().equalsIgnoreCase("SalesStaff")) {
            salesStaffRepository.deleteByAccountId(id);
        } else if (account.getRole().getName().equalsIgnoreCase("Admin")) {
            adminRepository.deleteByAccountId(id);
        }

        // Sau khi xóa entity phụ, mới được xóa account
        accountRepository.deleteById(id);
    }

    @Transactional
    public Account setActiveStatus(Integer id, boolean isActive) {
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
        acc.setIsActive(isActive);
        return accountRepository.save(acc);
    }

}