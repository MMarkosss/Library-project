package br.com.livraria.api_livraria.dto;

import java.util.List;

public record AutorDetalhesDTO(
        Long id,
        String nome,
        String biografia,
        List<LivroResumoDTO> livros // Reutilizando nosso DTO resumido para quebrar o loop infinito!
) {
}