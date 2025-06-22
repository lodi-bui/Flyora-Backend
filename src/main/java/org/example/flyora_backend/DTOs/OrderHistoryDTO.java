package org.example.flyora_backend.DTOs;

import lombok.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderHistoryDTO {
    private Integer orderId;
    private Timestamp orderDate;
    private Boolean status;
    private List<OrderDetailDTO> items;
}
