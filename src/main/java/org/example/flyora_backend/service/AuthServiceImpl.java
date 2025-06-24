package org.example.flyora_backend.service;

import java.util.Map;

import org.example.flyora_backend.DTOs.LoginDTO;
import org.example.flyora_backend.DTOs.LoginResponseDTO;
import org.example.flyora_backend.DTOs.RegisterDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final ShopOwnerRepository shopOwnerRepository;
    private final AdminRepository adminRepository;
    private final SalesStaffRepository salesStaffRepository;

    @Override
    public Map<String, Object> registerCustomer(RegisterDTO request) {
        if (accountRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER không tồn tại"));

        // 👉 Lưu mật khẩu thẳng (plain text)
        Account account = new Account();
        account.setUsername(request.getUsername());
        account.setPassword(request.getPassword()); // KHÔNG MÃ HÓA
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setRole(customerRole);
        account.setIsActive(true);
        account.setIsApproved(true);
        accountRepository.save(account);

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setAccount(account);
        customerRepository.save(customer);

        return Map.of("message", "Đăng ký thành công", "userId", customer.getId());
    }


    @Override
    public LoginResponseDTO loginCustomer(LoginDTO request) {
        Account account = accountRepository
                .findByUsernameAndPassword(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sai tên đăng nhập hoặc mật khẩu"));

        if (!account.getIsActive() || !account.getIsApproved()) {
            throw new RuntimeException("Tài khoản chưa được kích hoạt");
        }

        String roleName = account.getRole().getName();

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUserId(account.getId());
        response.setRole(roleName);

        // Lấy tên người dùng từ bảng tương ứng
        switch (roleName) {
            case "CUSTOMER" -> {
                Customer c = customerRepository.findByAccountId(account.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy customer"));
                response.setName(c.getName());
            }
            case "SHOP_OWNER" -> {
                ShopOwner s = shopOwnerRepository.findByAccountId(account.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy shop owner"));
                response.setName(s.getName());
            }
            case "ADMIN" -> {
                Admin a = adminRepository.findByAccountId(account.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy admin"));
                response.setName(a.getName());
            }
            case "STAFF" -> {
                SalesStaff staff = salesStaffRepository.findByAccountId(account.getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy staff"));
                response.setName(staff.getName());
            }
        }

        return response;
    }

}
