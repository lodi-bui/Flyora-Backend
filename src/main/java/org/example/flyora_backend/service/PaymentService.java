package org.example.flyora_backend.service;

import org.example.flyora_backend.DTOs.CreatePaymentDTO;
import org.example.flyora_backend.configuration.VNPAYConfig;
import org.example.flyora_backend.utils.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final VNPAYConfig vnPayConfig;

    public String createVnPayPayment(HttpServletRequest request, CreatePaymentDTO dto) {
        long amount = dto.getAmount() * 100L;

        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig();
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        
        if (dto.getBankCode() != null && !dto.getBankCode().isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", dto.getBankCode());
        }

        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnPayConfig.getSecretKey(), hashData);

        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;

        return vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
    }
}
