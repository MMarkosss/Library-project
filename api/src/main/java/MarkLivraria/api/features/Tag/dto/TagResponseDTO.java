package MarkLivraria.api.features.Tag.dto;

import MarkLivraria.api.features.Tag.Tag;

public record TagResponseDTO(Long id, String nome) {
    // Construtor inteligente para converter a Entidade em DTO e evitar o Loop Infinito!
    public TagResponseDTO(Tag tag) {
        this(tag.getId(), tag.getNome());
    }
}
