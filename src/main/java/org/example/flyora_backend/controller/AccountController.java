package org.example.flyora_backend.controller;

import org.example.flyora_backend.model.response.ResponseObject;
import org.example.flyora_backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/account")
@RestController
public class AccountController {

    @Autowired
    private AccountService accounttService;

    @GetMapping("")
    public ResponseEntity<ResponseObject> getAllAccounts() {
        try {
            return ResponseObject.APIResponse(400, "Get Product Success !", HttpStatus.OK, accounttService.getAllAccounts());
        } catch (Exception e) {
            return ResponseObject.APIResponse(400, "Get Product failed !", HttpStatus.BAD_REQUEST, null);
        }
    }
}
