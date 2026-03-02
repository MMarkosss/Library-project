package br.com.livraria.api_livraria.dto;

import jakarta.validation.constraints.Positive;

public record DescontoDTO(
        @Positive(message = "O percentual de desconto deve ser maior que zero")
        double percentual // Ex: 0.10 para 10%, ou 10.0 dependendo de como você programou a sua interface!
) {
}