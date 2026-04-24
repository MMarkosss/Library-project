package MarkLivraria.api.features.Promocional.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record DescontoRequestDTO(
        @Positive(message = "A porcentagem de desconto deve ser maior que zero")
        BigDecimal percentual
) {}
