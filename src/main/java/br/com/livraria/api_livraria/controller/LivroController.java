package br.com.livraria.api_livraria.controller;

import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

@RestController // 1. Diz que essa classe recebe requisições HTTP (JSON)
@RequestMapping("/livros") // 2. Define o endereço base: localhost:8080/livros
public class LivroController {

    @Autowired
    private LivroService service;

    // GET: Para quem acessar via navegador ou Postman
    @GetMapping
    public List<Livro> listar() {
        return service.listarTodos();
    }

    // POST: Para quem quiser enviar dados para salvar
    @PostMapping
    public Livro criar(@RequestBody Livro livro) {
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
}