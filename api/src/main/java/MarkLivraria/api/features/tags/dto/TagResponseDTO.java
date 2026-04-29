package MarkLivraria.api.features.tags.dto;

import MarkLivraria.api.features.tags.Tag;

public record TagResponseDTO(Long id, String name) {
    // Construtor inteligente para converter a Entidade em DTO e evitar o Loop Infinito!
    public TagResponseDTO(Tag tag) {
        this(tag.getId(), tag.getName());
    }
}
