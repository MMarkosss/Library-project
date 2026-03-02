package br.com.livraria.api_livraria.controller;

import br.com.livraria.api_livraria.model.Autor;
import br.com.livraria.api_livraria.repository.AutorRepository;
import br.com.livraria.api_livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.livraria.api_livraria.dto.AutorDetalhesDTO;
import br.com.livraria.api_livraria.dto.LivroResumoDTO;
import br.com.livraria.api_livraria.service.AutorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

@RestController
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private AutorService autorService;

    @GetMapping("/{id}")
    public ResponseEntity<AutorDetalhesDTO> buscarUm(@PathVariable Long id) {
        // 1. Busca o autor de verdade
        Autor autor = autorService.buscarPorId(id);

        // 2. Transforma a lista de Livros gordos em uma lista de LivroResumoDTO
        List<LivroResumoDTO> livrosDTO = autor.getLivros().stream()
                .map(LivroResumoDTO::new)
                .toList();

        // 3. Monta a caixinha final do Autor
        AutorDetalhesDTO resposta = new AutorDetalhesDTO(
                autor.getId(),
                autor.getNome(),
                autor.getBiografia(),
                livrosDTO
        );

        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Autor> cadastrar(@RequestBody Autor autor) {
        Autor autorSalvo = autorRepository.save(autor);
        return ResponseEntity.ok(autorSalvo);
    }
}