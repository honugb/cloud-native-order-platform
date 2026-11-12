package com.honugb.order_service.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderRequest(

    @NotBlank(message = "Product name is required")
    String productName,

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    Integer quantity,

    @NotNull(message = "Unit price is required")
    @DecimalMin(
        value = "0.01",
        message = "Unit price must be greater than zero"
    )
    BigDecimal unitPrice

) {
}