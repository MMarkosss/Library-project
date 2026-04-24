package MarkLivraria.api.features.Livro;

import MarkLivraria.api.features.Livro.dto.LivroRequestDTO;
import MarkLivraria.api.features.Promocional.dto.DescontoRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import MarkLivraria.api.features.Livro.dto.LivroResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Diz que essa classe recebe requisições HTTP (JSON)
@RequiredArgsConstructor
@RequestMapping("/livros") // 2. Define o endereço base: localhost:8080/livros
@Tag(name = "Catálogo de Livros", description = "Operações para gerenciamento de livros e e-books da loja")
public class LivroController {

    private final LivroService livroService;

    // GET: Para quem acessar via navegador ou Postman
    @GetMapping
    public Page<LivroResponseDTO> listar(@PageableDefault(size = 10, sort = {"titulo"}) Pageable page) {
        // Vai ao banco buscar a PÁGINA de livros (ex: 10 livros)
        Page<Livro> pagesbook = livroService.pagesLivros(page);

        // Converte a página de Livro para uma página de DTO
        return pagesbook.map(LivroResponseDTO::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> buscarUm(@PathVariable Long id) {
        //  Pede para o service buscar (Se não existir, o erro 404 é lançado lá dentro e o Controller nem fica sabendo)
        Livro livroExiste = livroService.buscaLivroPorId(id);

        //  Converte para a caixinha de response e Retorna 200 OK
        return ResponseEntity.ok(new LivroResponseDTO(livroExiste));
    }

    // POST: Para quem quiser enviar dados para salvarLivro
    @PostMapping
    public ResponseEntity<LivroResponseDTO> cadastrar(@RequestBody @Valid LivroRequestDTO dto) {
        // 1. Manda o DTO para o Service fazer o trabalho sujo
        Livro livroSalvo = livroService.salvarLivro(dto);

        // 2. Transforma o Livro salvo no DTO de Saída e devolve Status 201 (Created)
        return ResponseEntity.status(201).body(new LivroResponseDTO(livroSalvo));
    }

    // DELETE: Para remover um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        // 1. Manda o service deletarLivro
        livroService.deletarLivro(id);

        // 2. Se deu tudo certo, retorna o 204 No Content
        return ResponseEntity.noContent().build();
    }

    // PUT: Para atualizarLivro um livro existente
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequestDTO dto) {
        // Manda o gerente trabalhar
        Livro livroAtualizado = livroService.atualizarLivro(id, dto);

        // Empacota a resposta e Devolve 200 OK
        return ResponseEntity.ok(new LivroResponseDTO(livroAtualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LivroResponseDTO> atualizarParcial(@PathVariable Long id, @RequestBody LivroRequestDTO dto) {
        Livro livroAtualizado = livroService.atualizarParcial(id, dto);

        return ResponseEntity.ok(new LivroResponseDTO(livroAtualizado));
    }

    @PutMapping("/{id}/aplicar-desconto")
    @Operation(summary = "Aplica um desconto promocional",
            description = "Verifica se o livro implementa a interface Promocional e aplica o porcentagem de desconto no preço. Retorna erro 400 se o livro não aceitar descontos.")
    public ResponseEntity<LivroResponseDTO> aplicarDesconto(@PathVariable Long id, @Valid @RequestBody DescontoRequestDTO dto) {
        // Manda o Service tentar aplicar o desconto
        Livro livroComDesconto = livroService.aplicarDesconto(id, dto);

        // Converte o livro com o novo preço para a caixinha de resumo
        return ResponseEntity.ok(new LivroResponseDTO(livroComDesconto));
    }
}
