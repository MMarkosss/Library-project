package MarkLivraria.api.features.Livro.dto;

import MarkLivraria.api.features.Livro.Livro;
import MarkLivraria.api.features.Tag.Tag;

import java.math.BigDecimal;
import java.util.List;

// O Record já cria os getters, toString e equals automaticamente nos bastidores!
public record LivroResponseDTO(
        Long id,
        String titulo,
        BigDecimal preco,
        String tipo,
        List<String> tags // <-- Adicionamos a lista de strings para mostrar os nomes das tags
) {
    // ESTA É A MÁGICA: Um construtor que recebe a Entidade e repassa os dados para o construtor principal do Record
    public LivroResponseDTO(Livro livro) {
        this(
                livro.getId(),
                livro.getTitulo(),
                livro.getPreco(),
                livro.getClass().getSimpleName(),
                // Usamos Streams para transformar a List<Tag> em uma List<String> (apenas os nomes)
                livro.getTags().stream().map(Tag::getNome).toList()
        );
    }
}
