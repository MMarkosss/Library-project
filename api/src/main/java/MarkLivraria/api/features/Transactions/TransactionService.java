package MarkLivraria.api.features.Transactions;

import MarkLivraria.api.features.Livro.Fisico;
import MarkLivraria.api.features.Livro.LivroRepository;
import MarkLivraria.api.features.Livro.LivroService;
import MarkLivraria.api.features.Usuario.Usuario;
import MarkLivraria.api.features.Usuario.UsuarioRepository;
import MarkLivraria.api.features.compra.Compra;
import MarkLivraria.api.features.compra.CompraRepository;
import MarkLivraria.api.features.emprestimo.Emprestimo;
import MarkLivraria.api.features.emprestimo.EmprestimoRepository;
import MarkLivraria.api.features.emprestimo.StatusEmprestimo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final CompraRepository compraRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final LivroService livroService;
    private final UsuarioRepository usuarioRepository;
    private final LivroRepository livroRepository;

    @Transactional
    public void efetuarCompra(String emailUsuario, Long livroId) {

        // 1. Buscamos quem está comprando e o que está sendo comprado
        var usuario = (Usuario) usuarioRepository.findByLogin(emailUsuario);
        var livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        // 2. Regra de Negócio: Verificação de Estoque (Apenas para livros físicos)
        if (livro instanceof Fisico fisico) {
            if (fisico.getQuantidadeEstoque() <= 0) {
                throw new IllegalStateException("Estoque esgotado para o livro físico selecionado.");
            }
            // Retira 1 do estoque
            fisico.setQuantidadeEstoque(fisico.getQuantidadeEstoque() - 1);
            livroRepository.save(fisico);
        }

        // 3. Efetiva a compra (Ebooks passam direto pra cá)
        var compra = new Compra();
        compra.setUsuario(usuario);
        compra.setLivro(livro);
        compra.setDataCompra(LocalDateTime.now());
        compra.setValorPago(livro.getPreco());

        compraRepository.save(compra);
    }

    @Transactional
    public void efetuarEmprestimo(String emailUsuario, Long livroId) {

        var usuario = (Usuario) usuarioRepository.findByLogin(emailUsuario);
        var livro = livroRepository.findById(livroId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado."));

        // Regra de Negócio: Não emprestamos Ebooks, apenas Físicos
        if (!(livro instanceof Fisico fisico)) {
            throw new IllegalArgumentException("Apenas livros físicos podem ser emprestados.");
        }

        // Regra de Negócio: Tem na prateleira?
        if (fisico.getQuantidadeEstoque() <= 0) {
            throw new IllegalStateException("Todos os exemplares deste livro já estão emprestados ou vendidos.");
        }

        // Retira 1 da prateleira
        fisico.setQuantidadeEstoque(fisico.getQuantidadeEstoque() - 1);
        livroRepository.save(fisico);

        // Gera o contrato de empréstimo (Dura 14 dias)
        var emprestimo = new Emprestimo();
        emprestimo.setUsuario(usuario);
        emprestimo.setLivro(livro);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(14));
        emprestimo.setStatus(StatusEmprestimo.ATIVO);

        emprestimoRepository.save(emprestimo);
    }
}