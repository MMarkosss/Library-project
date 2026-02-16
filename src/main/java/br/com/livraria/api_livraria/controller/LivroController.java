package br.com.livraria.api_livraria.controller;

import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;
import br.com.livraria.api_livraria.dto.LivroResumoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;


@RestController // 1. Diz que essa classe recebe requisições HTTP (JSON)
@RequestMapping("/livros") // 2. Define o endereço base: localhost:8080/livros
public class LivroController {

    @Autowired
    private LivroService service;

    // GET: Para quem acessar via navegador ou Postman
    @GetMapping
    public Page<LivroResumoDTO> listar(@PageableDefault(size = 10, sort = {"titulo"}) Pageable paginacao) {

        // 1. Vai ao banco buscar a PÁGINA de livros (ex: 10 livros)
        Page<Livro> paginaDeLivros = service.listarTodos(paginacao);

        // 2. Converte a página de Livro para uma página de DTO
        return paginaDeLivros.map(livro -> new LivroResumoDTO(livro.getId(), livro.getTitulo(), livro.getPreco()));
    }

    // POST: Para quem quiser enviar dados para salvar
    @PostMapping
    public Livro criar(@Valid @RequestBody Livro livro) {
        return service.cadastrar(livro);

    }

    // Importe org.springframework.http.ResponseEntity;
// Importe java.util.Optional;

    @GetMapping("/{id}") // A URL será localhost:8080/livros/1
    public ResponseEntity<Livro> buscarUm(@PathVariable Long id) {
        // 1. Chama o service
        Optional<Livro> caixa = service.buscarPorId(id);

        // 2. Verifica se tem algo na caixa
        if (caixa.isPresent()) {
            // Se tem, retorna 200 OK com o livro dentro
            return ResponseEntity.ok(caixa.get());
        } else {
            // Se não tem, retorna 404 Not Found (sem corpo)
            return ResponseEntity.notFound().build();
        }
    }
    // DELETE: Para remover um livro
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        // Aproveitamos o método que já criamos para checar se existe
        if (service.buscarPorId(id).isPresent()) {
            service.deletar(id);
            return ResponseEntity.noContent().build(); // Retorna 204 No Content
        }
        return ResponseEntity.notFound().build(); // Retorna 404 se não achar
    }
    // PUT: Para atualizar um livro existente
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id,@Valid @RequestBody Livro livroAtualizado) {

        // 1. Verificamos se o livro existe no banco
        if (service.buscarPorId(id).isPresent()) {

            // 2. Garantimos que o ID do objeto é o mesmo da URL
            livroAtualizado.setId(id);

            // 3. Mandamos salvar (como já tem ID, o Spring fará um UPDATE)
            Livro livroSalvo = service.cadastrar(livroAtualizado);

            return ResponseEntity.ok(livroSalvo); // Retorna 200 OK com os dados novos
        }

        // Se não achar o livro, retorna 404
        return ResponseEntity.notFound().build();
    }
}