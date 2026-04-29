package MarkLivraria.api.features.books.dto;

import MarkLivraria.api.features.books.Genre;
import MarkLivraria.api.features.books.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record BookRequestDTO(

        @NotNull(message = "O tipo é obrigatório")
        Type type,
        @NotBlank (message = "O titulo é obrigatorio")
        String title,
        @NotNull (message = "O ID do authors não pode ser estar vazio")
        Long authorId,
        @Positive (message = "O preço deve assumir valores positivos!")
        @NotNull(message = "O preço não pode ser nulo")
        BigDecimal price,
        @Positive (message = "O books deve assumir valores positivos!")
        @NotNull(message = "As paginas não pode ter valor nulo")
        Integer pages,
        @NotNull (message = "O books deve conter o genero!")
        Genre genre,
        LocalDate publicationDate,
        String watermark,
        Double sizeMb,
        Integer weightGrams,
        Integer quantity,
        List<Long> tagIds
) {}
