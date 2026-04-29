package MarkLivraria.api.features.promotional.dto;


import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DiscountRequestDTO(
        @Positive(message = "A porcentagem de desconto deve ser maior que zero")
        BigDecimal percentage
) {}
