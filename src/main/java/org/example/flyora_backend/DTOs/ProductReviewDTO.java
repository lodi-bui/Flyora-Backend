package org.example.flyora_backend.DTOs;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDTO {

    @NotNull
    private Integer customerId;

    @NotNull
    private Integer productId;

    @Min(1)
    @Max(5)
    private int rating;

    @Size(max = 500)
    private String comment;
}
