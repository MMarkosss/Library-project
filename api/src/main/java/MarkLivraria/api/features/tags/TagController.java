package MarkLivraria.api.features.tags;

import MarkLivraria.api.features.tags.dto.TagResponseDTO;
import MarkLivraria.api.features.tags.dto.TagRequestDTO;
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
    public ResponseEntity<TagResponseDTO> findOneTag (@PathVariable Long id) {
        Tag existingTag = tagService.findTagById(id);

        return ResponseEntity.ok(new TagResponseDTO(existingTag));
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> listTags (String name) {
        List<Tag> listedTags = tagService.listTags(name);

        return ResponseEntity.ok(listedTags.stream().map(TagResponseDTO::new).toList());
    }

    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag (@RequestBody @Valid TagRequestDTO dto) {
        Tag savedTag = tagService.saveTag(dto);

        return ResponseEntity.status(201).body(new TagResponseDTO(savedTag));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag (@PathVariable Long id, @RequestBody @Valid TagRequestDTO dto) {
        Tag updatedTag = tagService.updateTag(id, dto);

        return ResponseEntity.ok(new TagResponseDTO(updatedTag));
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Void> deleteTag (@PathVariable Long id) {
        tagService.deleteTag(id);

        return ResponseEntity.noContent().build();
    }
}
