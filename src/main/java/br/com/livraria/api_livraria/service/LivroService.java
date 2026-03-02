package br.com.livraria.api_livraria.service;

// Importe as classes necessárias
import br.com.livraria.api_livraria.dto.LivroCadastroDTO;
import br.com.livraria.api_livraria.model.Autor;
import br.com.livraria.api_livraria.model.Ebook;
import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.repository.AutorRepository;
import br.com.livraria.api_livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.EntityNotFoundException;
// Importe a interface Promocional se precisar
import br.com.livraria.api_livraria.model.Promocional;
import br.com.livraria.api_livraria.dto.DescontoDTO;


@Service // 1. "Spring, gerencie essa classe pra mim"
public class LivroService {

    @Autowired // 2. "Spring, me dá aquele Repository pronto que você criou"
    private LivroRepository livroRepository;

    @Autowired
    private AutorRepository autorRepository; // 1. Trazemos o repositório de autores para cá



    public Livro aplicarDesconto(Long id, DescontoDTO dto) {
        // 1. Busca o livro no banco
        Livro livro = livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        // 2. A MÁGICA: Verifica se a instância desse livro assinou o contrato Promocional
        if (livro instanceof Promocional promocional) {

            // 3. Tenta aplicar o desconto. Supondo que o seu método retorne um boolean:
            boolean descontoAplicado = promocional.aplicaDescontoDe(dto.percentual());

            if (!descontoAplicado) {
                // Se a interface barrou (ex: desconto maior que o limite permitido)
                throw new IllegalArgumentException("O valor do desconto excedeu o limite permitido para este tipo de livro.");
            }

            // Se deu tudo certo, salva o livro com o novo preço no banco
            return livroRepository.save(livro);

        } else {
            // Se o livro não implementa a interface Promocional
            throw new IllegalArgumentException("Este livro não é promocional e não aceita descontos.");
        }
    }


    public Livro atualizar(Long id, LivroCadastroDTO dto) {

        // 1. Busca o livro. Se não achar, lança o erro (Isso substitui o "if" do Controller)
        Livro livroExistente = livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        // 2. Busca o autor.
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado!"));

        // 3. Atualiza os dados
        livroExistente.setTitulo(dto.titulo());
        livroExistente.setPreco(dto.preco());
        livroExistente.setPaginas(dto.paginas());
        livroExistente.setGenero(dto.genero());
        livroExistente.setAutor(autor);

        // 4. Salva com apenas 1 ida ao banco
        return livroRepository.save(livroExistente);
    }

    public Livro salvar(LivroCadastroDTO dto) {
        // 2. Busca o Autor de verdade no banco usando o ID que veio no DTO
        Autor autor = autorRepository.findById(dto.autorId())
                .orElseThrow(() -> new RuntimeException("Autor não encontrado no banco de dados!"));

        // 3. Verifica qual é o tipo do livro para instanciar a classe correta
        Livro novoLivro;
        if ("EBOOK".equalsIgnoreCase(dto.tipo())) {
            Ebook ebook = new Ebook();
            ebook.setMarcaDagua(dto.marcaDagua());
            novoLivro = ebook;
        } else {
            novoLivro = new Livro();
        }

        // 4. Copia os dados do DTO para a Entidade
        novoLivro.setTitulo(dto.titulo());
        novoLivro.setPreco(dto.preco());
        novoLivro.setPaginas(dto.paginas());
        novoLivro.setGenero(dto.genero());
        novoLivro.setDataLancamento(dto.dataLancamento());

        // 5. Conecta o Autor completo ao Livro!
        novoLivro.setAutor(autor);

        // 6. Salva e devolve
        return livroRepository.save(novoLivro);
    }

    // Método para LISTAR todos os livros
    public Page<Livro> listarTodos(Pageable paginacao) {
        return livroRepository.findAll(paginacao);
    }

    // 1. O buscarPorId agora retorna o Livro direto ou lança o erro (Exatamente como fizemos no atualizar)
    public Livro buscarPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }

    // 2. O deletar fica muito mais inteligente!
    public void deletar(Long id) {
        // Ele tenta buscar o livro usando o método de cima.
        // Se não existir, o buscarPorId já lança o erro 404 e a execução para aqui mesmo!
        Livro livro = buscarPorId(id);

        // Se passou da linha de cima, é porque o livro existe, então podemos deletar com segurança.
        livroRepository.delete(livro);
    }
    
}