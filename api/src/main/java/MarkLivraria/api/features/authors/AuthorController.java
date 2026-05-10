package MarkLivraria.api.features.authors;

import MarkLivraria.api.features.authors.dto.AuthorRequestDTO;
import MarkLivraria.api.features.authors.dto.AuthorResponseDTO;
import MarkLivraria.api.features.authors.dto.AuthorSimpleResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autores")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> findOneAuthor(@PathVariable Long id) {
        Author author = authorService.findAuthorById(id);

        return ResponseEntity.ok(new AuthorResponseDTO(author));
    }

    @GetMapping
    public ResponseEntity<Page<AuthorSimpleResponse>> listCustomAuthor (
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String bookTitle,
            @PageableDefault(size = 10, sort = {"name"}) Pageable pageable) {

        Page <Author> authors = authorService.listAuthors(name,bookTitle,pageable);

        Page<AuthorSimpleResponse> response = authors.map(AuthorSimpleResponse::new);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AuthorResponseDTO> createAuthor (@RequestBody @Valid AuthorRequestDTO dto) {
        Author author = authorService.saveAuthor(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthorResponseDTO(author));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorSimpleResponse> updateAuthor (@PathVariable Long id, AuthorRequestDTO dto) {
        Author updatedAuthor = authorService.updateAuthor(id, dto);

        return ResponseEntity.ok(new AuthorSimpleResponse(updatedAuthor));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AuthorResponseDTO> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> fields) {

        Author updatedAuthor = authorService.partialUpdate(id, fields);

        return ResponseEntity.ok(new AuthorResponseDTO(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor (@PathVariable Long id) {
        authorService.deleteAuthor(id);

        return ResponseEntity.noContent().build();
    }
}
