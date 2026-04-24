package MarkLivraria.api.features.Autor.dto;

import jakarta.validation.constraints.NotBlank;

public record AutorRequestDTO(
        @NotBlank  (message = "O campo nome é obrigatorio")
        String nome,

        @NotBlank
        String biografia
){}
