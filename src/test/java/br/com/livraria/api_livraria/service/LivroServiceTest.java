package br.com.livraria.api_livraria.service;

import br.com.livraria.api_livraria.dto.DescontoDTO;
import br.com.livraria.api_livraria.model.Ebook;
import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.repository.LivroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita a mágica do Mockito
class LivroServiceTest {

    @Mock
    private LivroRepository repository; // O banco de dados FALSO

    @InjectMocks
    private LivroService service; // O nosso Gerente REAL, mas injetado com o banco falso

    // O nosso teste virá aqui dentro!
    @Test
    @DisplayName("Deve lançar exceção ao tentar aplicar desconto em um livro que não é promocional")
    void naoDeveAplicarDescontoEmLivroFisico() {
        // 1. DADO (Arrange): Preparamos o cenário
        Long idLivro = 1L;
        Livro livroFisico = new Livro(); // Se sua classe se chamar LivroFisico, ajuste aqui
        livroFisico.setId(idLivro);
        livroFisico.setPreco(100.0);

        DescontoDTO dto = new DescontoDTO(10); // 10% de desconto

        // Treinamos o nosso banco falso: "Quando alguém buscar o ID 1, devolva esse livro físico falso"
        when(repository.findById(idLivro)).thenReturn(Optional.of(livroFisico));

        // 2. QUANDO (Act) e 3. ENTÃO (Assert): Executamos e verificamos a explosão controlada
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            service.aplicarDesconto(idLivro, dto);
        });

        // Verificamos se a mensagem do erro é exatamente a que programamos no Service!
        assertEquals("Este livro não é promocional e não aceita descontos.", erro.getMessage());


    }

    @Test
    @DisplayName("Deve aplicar desconto corretamente em um Ebook")
    void deveAplicarDescontoEmEbook() {
        // 1. DADO (Arrange): Preparamos um Ebook de R$ 100,00
        Long idLivro = 2L;
        // Ajuste para o nome exato da sua classe Ebook
        Livro ebook = new Ebook();
        ebook.setId(idLivro);
        ebook.setPreco(100.0);

        DescontoDTO dto = new DescontoDTO(10); // 10% de desconto

        // Treinamos o banco: "Ao buscar o ID 2, devolva o Ebook"
        when(repository.findById(idLivro)).thenReturn(Optional.of(ebook));

        // Treinamos o banco: "Ao mandar salvar qualquer livro, apenas devolva o próprio livro salvo"
        // (Isso é necessário porque o Service chama o repository.save() no final do processo!)
        when(repository.save(ebook)).thenReturn(ebook);

        // 2. QUANDO (Act): O Gerente tenta aplicar o desconto
        Livro livroAtualizado = service.aplicarDesconto(idLivro, dto);

        // 3. ENTÃO (Assert): O preço deve ter caído para 90.0!
        assertEquals(90.0, livroAtualizado.getPreco(), "O preço do Ebook deveria ter caído 10% (de 100 para 90)");
    }

}
