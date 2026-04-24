package MarkLivraria.api.features.Autor;

import MarkLivraria.api.features.Autor.dto.AutorResponseDTO;
import MarkLivraria.api.features.Autor.dto.AutorRequestDTO;
import MarkLivraria.api.features.Autor.dto.AutorSimpleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponseDTO> buscarUmAutor(@PathVariable Long id) {
        Autor autor = autorService.buscaAutorPorId(id);

        return ResponseEntity.ok(new AutorResponseDTO(autor));
    }

    @GetMapping
    public ResponseEntity<List<AutorSimpleResponse>> listarTudoAutor() {
        List<AutorSimpleResponse> listaTodosAutores = autorService.listarAutores().stream().map(AutorSimpleResponse::new).toList();

        return ResponseEntity.ok(listaTodosAutores);
    }

    @PostMapping
    public ResponseEntity<AutorResponseDTO> cadastroAutor (@RequestBody @Valid AutorRequestDTO dto) {
        Autor autor = autorService.salvarAutor(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AutorResponseDTO(autor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorSimpleResponse> atualizaAutor (@PathVariable Long id, AutorRequestDTO dto) {
        Autor autorAtualizado = autorService.atualizaAutor(id,dto);

        return ResponseEntity.ok(new AutorSimpleResponse(autorAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletaAutor (@PathVariable Long id) {
        autorService.deletaAutor(id);

        return ResponseEntity.noContent().build();
    }
}
