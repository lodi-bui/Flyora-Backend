package org.example.flyora_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import org.example.flyora_backend.model.UserDTO;
import org.example.flyora_backend.model.UserDTO.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class LoginController {

    @Autowired
    private UserDAO dao;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> credentials, HttpSession session) {        
        String userID = credentials.get("userID");
        String password = credentials.get("password");
        Map<String, Object> response = new HashMap<>();

        UserDTO loginUser = dao.checkLogin(userID, password);

        if (loginUser != null) {
            Role role = loginUser.getRole();  // ✅ Đây là Enum, không phải String

            // Kiểm tra role có thuộc 5 loại hợp lệ không (ở đây thì Enum chỉ có 5, nên check != null là đủ)
            if (role != null) {
                session.setAttribute("LOGIN_USER", loginUser);

                response.put("status", "success");
                response.put("role", role.name()); // Trả ra dưới dạng String
                response.put("user", loginUser);
            } else {
                response.put("status", "fail");
                response.put("message", "Role is not authorized.");
            }

        } else {
            response.put("status", "fail");
            response.put("message", "Incorrect UserID or Password.");
        }

        return response;
    }
}
