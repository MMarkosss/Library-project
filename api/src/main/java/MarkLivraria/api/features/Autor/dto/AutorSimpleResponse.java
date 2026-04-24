package MarkLivraria.api.features.Autor.dto;

import MarkLivraria.api.features.Autor.Autor;

public record AutorSimpleResponse(Long id, String nome) {
    public AutorSimpleResponse(Autor autor) {
        this(autor.getId(), autor.getNome());
    }
}
