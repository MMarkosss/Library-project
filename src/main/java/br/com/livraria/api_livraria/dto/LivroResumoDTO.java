package br.com.livraria.api_livraria.dto;

import br.com.livraria.api_livraria.model.Livro;

// O Record já cria os getters, toString e equals automaticamente nos bastidores!
public record LivroResumoDTO(
        Long id,
        String titulo,
        double preco,
        String tipo
) {
    // ESTA É A MÁGICA: Um construtor que recebe a Entidade e repassa os dados para o construtor principal do Record
    public LivroResumoDTO(Livro livro) {
        this(
                livro.getId(),
                livro.getTitulo(),
                livro.getPreco(),
                livro.getClass().getSimpleName() // A reflexão fica escondida aqui!
        );
    }
}
