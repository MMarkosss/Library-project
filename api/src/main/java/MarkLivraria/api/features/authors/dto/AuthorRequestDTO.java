package MarkLivraria.api.features.authors.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDTO(
        @NotBlank  (message = "O campo nome é obrigatorio")
        String name,

        @NotBlank
        String biography
){}
