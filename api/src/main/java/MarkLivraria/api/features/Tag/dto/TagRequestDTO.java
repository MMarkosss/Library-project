package MarkLivraria.api.features.Tag.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TagRequestDTO(
        @NotBlank (message = "O campo nome é obrigatorio")
        String nome,
        String descricao,
        List<Long>tagIds
){}
