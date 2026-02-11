package br.com.livraria.api_livraria.service;

import br.com.livraria.api_livraria.model.Livro;
import br.com.livraria.api_livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // 1. "Spring, gerencie essa classe pra mim"
public class LivroService {

    @Autowired // 2. "Spring, me dá aquele Repository pronto que você criou"
    private LivroRepository repository;

    // Método para SALVAR um livro no banco
    public Livro cadastrar(Livro livro) {
        // Aqui poderíamos ter regras (ex: validar se paginas > 0)
        // Se estiver tudo ok, mandamos o repository gravar
        return repository.save(livro);
    }

    // Método para LISTAR todos os livros
    public List<Livro> listarTodos() {
        return repository.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return repository.findById(id);
    }
}