package MarkLivraria.api.features.authors.dto;

import MarkLivraria.api.features.authors.Author;
import MarkLivraria.api.features.books.dto.BookResponseDTO;

import java.util.List;

public record AuthorResponseDTO(
        Long id,
        String name,
        List<BookResponseDTO> books // Reutilizando nosso DTO resumido para quebrar o loop infinito!
) {

    public AuthorResponseDTO(Author author) {
        this(
                author.getId(),
                author.getName(),
                author.getBooks().stream().map(BookResponseDTO::new).toList()
        );
    }
}
