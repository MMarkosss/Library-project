package MarkLivraria.api.features.Autor.dto;

import MarkLivraria.api.features.Autor.Autor;
import MarkLivraria.api.features.Livro.dto.LivroResponseDTO;

import java.util.List;

public record AutorResponseDTO(
        Long id,
        String nome,
        String biografia,
        List<LivroResponseDTO> livros // Reutilizando nosso DTO resumido para quebrar o loop infinito!
) {

    public AutorResponseDTO(Autor autor) {
        this(
                autor.getId(),
                autor.getNome(),
                autor.getBiografia(),
                autor.getLivros().stream().map(LivroResponseDTO::new).toList()
        );
    }
}
