package MarkLivraria.api.features.tags.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TagRequestDTO(
        @NotBlank (message = "O campo nome é obrigatorio")
        String name,
        String description,
        List<Long>tagIds
){}
