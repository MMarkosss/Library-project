package br.com.livraria.api_livraria.controller;

import br.com.livraria.api_livraria.dto.DescontoDTO;
import br.com.livraria.api_livraria.dto.LivroCadastroDTO;
import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import br.com.livraria.api_livraria.dto.LivroResumoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;


@RestController // 1. Diz que essa classe recebe requisições HTTP (JSON)
@RequestMapping("/livros") // 2. Define o endereço base: localhost:8080/livros
@Tag(name = "Catálogo de Livros", description = "Operações para gerenciamento de livros e e-books da loja")
public class LivroController {

    @Autowired
    private LivroService service;

    // GET: Para quem acessar via navegador ou Postman
    @GetMapping
    public Page<LivroResumoDTO> listar(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao) {

        // 1. Vai ao banco buscar a PÁGINA de livros (ex: 10 livros)
        Page<Livro> paginaDeLivros = service.listarTodos(paginacao);

        // 2. Converte a página de Livro para uma página de DTO
        return paginaDeLivros.map(livro -> new LivroResumoDTO(livro.getId(), livro.getTitulo(), livro.getPreco(),getClass().getSimpleName()));
    }

    // POST: Para quem quiser enviar dados para salvar
    @PostMapping
    public ResponseEntity<LivroResumoDTO> cadastrar(@RequestBody @Valid LivroCadastroDTO dto) {

        // 1. Manda o DTO para o Service fazer o trabalho sujo
        Livro livroSalvo = service.salvar(dto);


        // 2. Transforma o Livro salvo no DTO de Saída e devolve Status 201 (Created)
        LivroResumoDTO resposta = new LivroResumoDTO(livroSalvo);

        return ResponseEntity.status(201).body(resposta);
    }


    @GetMapping("/{id}")
    public ResponseEntity<LivroResumoDTO> buscarUm(@PathVariable Long id) {
        // 1. Pede para o service buscar (Se não existir, o erro 404 é lançado lá dentro e o Controller nem fica sabendo)
        Livro livro = service.buscarPorId(id);

        // 2. Converte para a caixinha de resumo
        LivroResumoDTO resposta = new LivroResumoDTO(livro);

        // 3. Retorna 200 OK
        return ResponseEntity.ok(resposta);
    }


    // DELETE: Para remover um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        // 1. Manda o service deletar
        service.deletar(id);

        // 2. Se deu tudo certo, retorna o 204 No Content
        return ResponseEntity.noContent().build();
    }


    // PUT: Para atualizar um livro existente
    @PutMapping("/{id}")
    public ResponseEntity<LivroResumoDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LivroCadastroDTO dto) {

        // 1. Manda o gerente trabalhar
        Livro livroAtualizado = service.atualizar(id, dto);

        // 2. Empacota a resposta
        LivroResumoDTO resposta = new LivroResumoDTO(livroAtualizado);

        // 3. Devolve 200 OK
        return ResponseEntity.ok(resposta);
    }


    @PutMapping("/{id}/aplicar-desconto")
    @Operation(summary = "Aplica um desconto promocional",
            description = "Verifica se o livro implementa a interface Promocional e aplica o percentual de desconto no preço. Retorna erro 400 se o livro não aceitar descontos.")
    public ResponseEntity<LivroResumoDTO> aplicarDesconto(@PathVariable Long id, @Valid @RequestBody DescontoDTO dto) {

        // Manda o Service tentar aplicar o desconto
        Livro livroComDesconto = service.aplicarDesconto(id, dto);

        // Converte o livro com o novo preço para a caixinha de resumo
        LivroResumoDTO resposta = new LivroResumoDTO(livroComDesconto);

        return ResponseEntity.ok(resposta);
    }
}