    package org.example.flyora_backend.controller;

    import java.util.Optional;

    import org.example.flyora_backend.DTOs.AccountDTO;
    import org.example.flyora_backend.model.Account;
    import org.example.flyora_backend.repository.AccountRepository;
    import org.example.flyora_backend.service.AccountService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    
    import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

    @RestController
    @RequestMapping("/api/v1/admin/accounts")
    @Tag(name = "Admin Services")
    public class AdminController {
        
        @Autowired
        private AccountRepository accountRepository;

        @Autowired
        private AccountService accountService;

        private void verifyAdmin(Integer requestAccountId) {
            Optional<Account> optionalAcc = accountRepository.findById(requestAccountId);        
            if(optionalAcc.isPresent()) {
                Account acc = optionalAcc.get();
                if(!acc.getRole().getName().equalsIgnoreCase("Admin")) {
                    throw new RuntimeException("Access denied");
                }
            }
        }

        @PostMapping
        @Operation(
            summary = "Tạo tài khoản mới",
            description = """
                Tạo mới tài khoản (chỉ dành cho Admin).

                ✅ Trường yêu cầu trong body (AccountDTO):
                - username (String)
                - password (String)
                - phone (String)
                - roleId (Integer): 1=ADMIN, 2=SHOPOWNER, 3=SALESSTAFF, 4=CUSTOMER
                - approvedBy (Integer): ID của admin duyệt

                📌 `requesterId` là ID của tài khoản gửi request, dùng để xác thực quyền admin.

                🔁 Trả về: Account đã tạo nếu thành công.
            """
        )
        public ResponseEntity<?> createAccount(@RequestBody AccountDTO dto, @RequestParam Integer requesterId) {
            verifyAdmin(requesterId);
            return ResponseEntity.ok(accountService.createAccount(dto));
        }
    }
