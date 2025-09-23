package com.gabsdev.orders_service.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequest(
        @NotBlank String userId,
        @NotEmpty List<String> items,
        @NotBlank String status
) {}