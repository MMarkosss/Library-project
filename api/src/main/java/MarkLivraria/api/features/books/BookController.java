package MarkLivraria.api.features.books;

import MarkLivraria.api.features.books.dto.BookRequestDTO;
import MarkLivraria.api.features.books.dto.BookResponseDTO;
import MarkLivraria.api.features.promotional.dto.DiscountRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // 1. Diz que essa classe recebe requisições HTTP (JSON)
@RequiredArgsConstructor
@RequestMapping("/livros") // 2. Define o endereço base: localhost:8080/livros
@Tag(name = "Catálogo de Livros", description = "Operações para gerenciamento de livros e e-books da loja")
public class BookController {

    private final BookService bookService;

    // GET: Para quem acessar via navegador ou Postman
    @GetMapping
    public ResponseEntity<Page<BookResponseDTO>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Genre genre,
            @PageableDefault(size = 10, sort = {"title"}) Pageable pageable) {

        Page<Book> books = bookService.listBooks(title, genre, pageable);

        // O Spring Data já sabe converter um Page<Livro> para Page<BookResponseDTO> usando o .map() nativo do Page
        Page<BookResponseDTO> response = books.map(BookResponseDTO::new);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDTO> findOne(@PathVariable Long id) {
        //  Pede para o service buscar (Se não existir, o erro 404 é lançado lá dentro e o Controller nem fica sabendo)
        Book existingBook = bookService.findBookById(id);

        //  Converte para a caixinha de response e Retorna 200 OK
        return ResponseEntity.ok(new BookResponseDTO(existingBook));
    }

    // POST: Para quem quiser enviar dados para salvarLivro
    @PostMapping
    public ResponseEntity<BookResponseDTO> create(@RequestBody @Valid BookRequestDTO dto) {
        // 1. Manda o DTO para o Service fazer o trabalho sujo
        Book savedBook = bookService.saveBook(dto);

        // 2. Transforma o Livro salvo no DTO de Saída e devolve Status 201 (Created)
        return ResponseEntity.status(201).body(new BookResponseDTO(savedBook));
    }

    // DELETE: Para remover um books
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        // 1. Manda o service deletarLivro
        bookService.deleteBook(id);

        // 2. Se deu tudo certo, retorna o 204 No Content
        return ResponseEntity.noContent().build();
    }

    // PUT: Para atualizarLivro um books existente
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDTO> update(@PathVariable Long id, @Valid @RequestBody BookRequestDTO dto) {
        // Manda o gerente trabalhar
        Book updatedBook = bookService.updateBook(id, dto);

        // Empacota a resposta e Devolve 200 OK
        return ResponseEntity.ok(new BookResponseDTO(updatedBook));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BookResponseDTO> partialUpdate(@PathVariable Long id, @RequestBody BookRequestDTO dto) {
        Book updatedBook = bookService.partialUpdate(id, dto);

        return ResponseEntity.ok(new BookResponseDTO(updatedBook));
    }

    @PutMapping("/{id}/aplicar-desconto")
    @Operation(summary = "Aplica um desconto promotional",
            description = "Verifica se o books implementa a interface promotional e aplica o porcentagem de desconto no preço. Retorna erro 400 se o books não aceitar descontos.")
    public ResponseEntity<BookResponseDTO> applyDiscount(@PathVariable Long id, @Valid @RequestBody DiscountRequestDTO dto) {
        // Manda o Service tentar aplicar o desconto
        Book discountedBook = bookService.applyDiscount(id, dto);

        // Converte o books com o novo preço para a caixinha de resumo
        return ResponseEntity.ok(new BookResponseDTO(discountedBook));
    }
}
