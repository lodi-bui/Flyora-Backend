package org.example.flyora_backend.DTOs;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentDTO {
    private Integer orderId;
    private Integer customerId;
    private Integer paymentMethodId;
}
