package MarkLivraria.api.features.books.dto;

import MarkLivraria.api.features.books.Book;
import MarkLivraria.api.features.tags.Tag;

import java.math.BigDecimal;
import java.util.List;

// O Record já cria os getters, toString e equals automaticamente nos bastidores!
public record BookResponseDTO(
        Long id,
        String title,
        BigDecimal price,
        String type,
        List<String> tags // <-- Adicionamos a lista de strings para mostrar os nomes das tags
) {
    // ESTA É A MÁGICA: Um construtor que recebe a Entidade e repassa os dados para o construtor principal do Record
    public BookResponseDTO(Book book) {
        this(
                book.getId(),
                book.getTitle(),
                book.getPrice(),
                book.getClass().getSimpleName(),
                // Usamos Streams para transformar a List<Tag> em uma List<String> (apenas os nomes)
                book.getTags().stream().map(Tag::getName).toList()
        );
    }
}
