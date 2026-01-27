package br.com.livraria.api_livraria.controller;

import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}