package br.com.livraria.api_livraria.dto;

// O Record já cria os getters, toString e equals automaticamente nos bastidores!
public record LivroResumoDTO(Long id, String titulo, double preco) {
}