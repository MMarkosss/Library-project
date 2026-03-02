package br.com.livraria.api_livraria.dto;

import br.com.livraria.api_livraria.model.Genero;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record LivroCadastroDTO(

        @NotBlank(message = "O tipo (FISICO ou EBOOK) é obrigatório")
        String tipo,

        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @Positive(message = "O preço deve ser maior que zero")
        double preco,

        @Positive(message = "O número de páginas deve ser maior que zero")
        int paginas,

        @NotNull(message = "O gênero é obrigatório")
        Genero genero,

        LocalDate dataLancamento,

        @NotNull(message = "O ID do autor é obrigatório")
        Long autorId, // A mágica está aqui: recebemos apenas o ID!


        String marcaDagua // Opcional, apenas para quando o tipo for EBOOK
) {
}