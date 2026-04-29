package MarkLivraria.api.features.books;


import MarkLivraria.api.features.authors.Author;
import MarkLivraria.api.features.authors.AuthorService;
import MarkLivraria.api.features.books.dto.BookRequestDTO;
import MarkLivraria.api.features.promotional.Promotional;
import MarkLivraria.api.features.promotional.dto.DiscountRequestDTO;
import MarkLivraria.api.features.tags.Tag;
import MarkLivraria.api.features.tags.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final TagRepository tagRepository;
    private final AuthorService authorService;


    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Page<Book> listBooks(String title, Genre genre, Pageable pageable) {

        // 1. Cria uma lista tipada para armazenar os filtros
        List<Specification<Book>> specs = new ArrayList<>();

        if (title != null && !title.isBlank()) {
            specs.add(BookSpecs.titleContains(title));
        }

        if (genre != null) {
            specs.add(BookSpecs.genreEquals(genre));
        }

        // 2. Funde todos os filtros da lista usando o AND
        Specification<Book> finalSpec = specs.stream()
                .reduce(Specification::and)
                .orElse(null); // Se não houver nenhum filtro, devolve null de forma segura.

        // 3. Executa a query
        return bookRepository.findAll(finalSpec, pageable);
    }

    public void fillCommonBookFields(Book filledBook, BookRequestDTO dto) {

        Author author = authorService.findAuthorById(dto.authorId());
        filledBook.setTitle(dto.title());
        filledBook.setAuthor(author);
        filledBook.setPrice(dto.price());
        filledBook.setGenre(dto.genre());
        filledBook.setPublicationDate(dto.publicationDate());
        filledBook.setPages(dto.pages());


        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            List<Tag> foundTags = tagRepository.findAllById(dto.tagIds());
            filledBook.setTags(foundTags);
        }
    }


    public Book saveBook(BookRequestDTO dto) {
        Book newBook = switch (dto.type()) {
            case Type.EBOOK -> {
                Ebook ebook = new Ebook();
                // Validação manual rápida da regra de negócio:
                if (dto.sizeMb() == null) {
                    throw new IllegalArgumentException("O ebook precisa de ter um tamanho em MB");
                }
                ebook.setSizeMb(dto.sizeMb());
                ebook.setWatermark(dto.watermark());
                yield ebook; // Retorna a instância para a variável 'novoLivro'
            }
            case Type.PHYSICAL -> {
                Physical physical = new Physical();
                if (dto.weightGrams() == null || dto.quantity() == null || dto.quantity() <= 0) {
                    throw new IllegalArgumentException("O livros físicos precisa ter peso e uma quantidade válida maior que zero");
                }
                physical.setWeightGrams(dto.weightGrams());
                physical.setStockQuantity(dto.quantity());
                yield physical;
            }
        };
        fillCommonBookFields(newBook,dto);

        return bookRepository.save(newBook);
    }

    public Book updateBook(Long id, BookRequestDTO dto) {
        // 1. Busca o books e o authors corretos
        Book existingBook = findBookById(id);

        // 2. Trava de Segurança: Impede a mudança do DNA do books
        boolean tryingToChangeType =
                (existingBook instanceof Physical && dto.type() == Type.EBOOK) ||
                        (existingBook instanceof Ebook && dto.type() == Type.PHYSICAL);

        if (tryingToChangeType) {
            throw new IllegalArgumentException("Não é possível alterar o formato do books (Físico/Ebook) após o cadastro. Cadastre um novo books.");
        }

        // 3. Atualiza os campos específicos das classes filhas
        if (existingBook instanceof Physical physical) {
            if (dto.weightGrams() == null || dto.quantity() == null || dto.quantity() < 0) {
                throw new IllegalArgumentException("O books físico precisa ter peso e uma quantidade válida (maior ou igual a zero).");
            }
            physical.setWeightGrams(dto.weightGrams());
            physical.setStockQuantity(dto.quantity()); // Agora o estoque atualiza perfeitamente!

        } else if (existingBook instanceof Ebook ebook) {
            if (dto.sizeMb() == null) {
                throw new IllegalArgumentException("O ebook precisa de ter um tamanho em MB.");
            }
            ebook.setSizeMb(dto.sizeMb());
            ebook.setWatermark(dto.watermark());
        }

        fillCommonBookFields(existingBook,dto);

        return bookRepository.save(existingBook);
    }

    public Book partialUpdate(Long id, BookRequestDTO dto) {
        Book existingBook = findBookById(id);

        // 1. Trava de Segurança do Type (Só avalia se o Front-end mandou o tipo no JSON)

        boolean tryingToChangeType =
                (existingBook instanceof Physical && dto.type() == Type.EBOOK) ||
                        (existingBook instanceof Ebook && dto.type() == Type.PHYSICAL);

        if (tryingToChangeType) {
            throw new IllegalArgumentException("Não é possível alterar o formato do books via PATCH, crie um novo books.");
        }


        // 2. Atualiza os campos específicos (Filhos) apenas se não forem nulos
        if (existingBook instanceof Physical physical) {
            if (dto.weightGrams() != null) {
                physical.setWeightGrams(dto.weightGrams());
            }
            if (dto.quantity() != null) {
                if (dto.quantity() < 0) {
                    throw new IllegalArgumentException("A quantidade não pode ser negativa.");
                }
                physical.setStockQuantity(dto.quantity());
            }

        } else if (existingBook instanceof Ebook ebook) {
            if (dto.sizeMb() != null) {
                ebook.setSizeMb(dto.sizeMb());
            }
            if (dto.watermark() != null) {
                ebook.setWatermark(dto.watermark());
            }
        }

        // 3. Atualiza os dados gerais (Pai) apenas se não forem nulos
        if (dto.title() != null) existingBook.setTitle(dto.title());
        if (dto.price() != null) existingBook.setPrice(dto.price());
        if (dto.pages() != null) existingBook.setPages(dto.pages());
        if (dto.genre() != null) existingBook.setGenre(dto.genre());
        if (dto.publicationDate() != null) existingBook.setPublicationDate(dto.publicationDate());

        // 4. Se mandou um novo authors, busca e atualiza
        if (dto.authorId() != null) {
            Author author = authorService.findAuthorById(dto.authorId());
            existingBook.setAuthor(author);
        }

        // 5. Se mandou novas Tags, atualiza
        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            List<Tag> foundTags = tagRepository.findAllById(dto.tagIds());
            existingBook.setTags(foundTags);
        }

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        Book existingBook = findBookById(id);
        bookRepository.delete(existingBook);
    }

    public Book applyDiscount(Long id, DiscountRequestDTO dto) {
        // 1. Busca o books no banco
        Book book = bookRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        // 2. A MÁGICA: Verifica se a instância desse books assinou o contrato promotional
        if (book instanceof Promotional promotional) {

            // 3. Tenta aplicar o desconto.
            boolean discountApplied = promotional.applyDiscountOf(dto.percentage());

            if (!discountApplied) {
                // Se a interface barrou (ex: desconto maior que o limite permitido)
                throw new IllegalArgumentException("O valor do desconto excedeu o limite permitido para este tipo de books.");
            }

            // Se deu tudo certo, salva o books com o novo preço no banco
            return bookRepository.save(book);

        } else {
            // Se o books não implementa a interface promotional
            throw new IllegalArgumentException("Este books não é promotional e não aceita descontos.");
        }
    }
}