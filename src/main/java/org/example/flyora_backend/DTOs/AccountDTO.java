package org.example.flyora_backend.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String username;
    private String password; 
    private String phone;

    private Boolean isActive;      // true = đã kích hoạt
    private Boolean isApproved;    // true = đã được duyệt

    private Integer roleId;        // dùng khi tạo mới (ví dụ: 1, 2, 3, 4)
    private String roleName;       // dùng để hiển thị (ví dụ: ADMIN, SHOPOWNER...)

    private Integer approvedBy;    // tham chiếu tới Admin.id (admin duyệt)
}