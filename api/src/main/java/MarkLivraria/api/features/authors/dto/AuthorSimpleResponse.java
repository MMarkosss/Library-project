package MarkLivraria.api.features.authors.dto;

import MarkLivraria.api.features.authors.Author;

public record AuthorSimpleResponse(Long id, String nome) {
    public AuthorSimpleResponse(Author author) {
        this(author.getId(), author.getName());
    }
}
