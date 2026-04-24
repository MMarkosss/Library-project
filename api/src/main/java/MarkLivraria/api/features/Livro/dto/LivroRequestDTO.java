package MarkLivraria.api.features.Livro.dto;

import MarkLivraria.api.features.Livro.Genero;

import MarkLivraria.api.features.Livro.Tipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record LivroRequestDTO(

        @NotNull(message = "O tipo é obrigatório")
        Tipo tipo,
        @NotBlank (message = "O titulo é obrigatorio")
        String titulo,
        @NotNull (message = "O ID do autor não pode ser estar vazio")
        Long autorId,
        @Positive (message = "O preço deve assumir valores positivos!")
        @NotNull(message = "O preço não pode ser nulo")
        BigDecimal preco,
        @Positive (message = "O livro deve assumir valores positivos!")
        @NotNull(message = "As paginas não pode ter valor nulo")
        Integer paginas,
        @NotNull (message = "O livro deve conter o genero!")
        Genero genero,
        LocalDate dataPublicacao,
        String marcaDagua,
        Double tamanhoMb,
        Integer pesoGramas,
        Integer quantidade,
        List<Long> tagIds
) {}
