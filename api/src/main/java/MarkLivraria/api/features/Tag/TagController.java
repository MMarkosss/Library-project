package MarkLivraria.api.features.Tag;

import MarkLivraria.api.features.Tag.dto.TagResponseDTO;
import MarkLivraria.api.features.Tag.dto.TagRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> buscaUmTag (@PathVariable Long id) {
        Tag tagExiste = tagService.buscaTagPorId(id);

        return ResponseEntity.ok(new TagResponseDTO(tagExiste));
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> listaTags () {
        List<Tag> tagsListadas = tagService.listaTags();

        return ResponseEntity.ok(tagsListadas.stream().map(TagResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<TagResponseDTO> cadastroTag (@RequestBody @Valid TagRequestDTO dto) {
        Tag tagSalva = tagService.salvaTag(dto);

        return ResponseEntity.status(201).body(new TagResponseDTO(tagSalva));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<TagResponseDTO> atualizadaTag (@PathVariable Long id, @RequestBody @Valid TagRequestDTO dto) {
        Tag tagExiste = tagService.buscaTagPorId(id);

        tagExiste.setNome(dto.nome());
        tagExiste.setDescricao(dto.descricao());

        return ResponseEntity.ok(new TagResponseDTO(tagExiste));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deletadaTag (@PathVariable Long id) {
        tagService.deletaTag(id);

        return ResponseEntity.noContent().build();
    }
}
