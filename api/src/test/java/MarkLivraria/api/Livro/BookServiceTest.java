package MarkLivraria.api.Livro;

import MarkLivraria.api.features.authors.Author;
import MarkLivraria.api.features.authors.AuthorService;
import MarkLivraria.api.features.books.*;
import MarkLivraria.api.features.books.dto.BookRequestDTO;
import MarkLivraria.api.features.promotional.dto.DiscountRequestDTO;
import MarkLivraria.api.features.tags.Tag;
import MarkLivraria.api.features.tags.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // Habilita a magica do Mockito
class BookServiceTest {

    @Mock
    private  BookRepository bookRepository; // O banco de dados FALSO
    @Mock
    private AuthorService authorService;
    @Mock
    private TagRepository tagRepository;


    /* 1. MELHORIA: Removemos o @InjectMocks.
     O nosso Service agora será instanciado manualmente! */
    private BookService bookService;

    // 2. MELHORIA: Setup explícito.
    // O JUnit roda este método antes de CADA UM dos @Test.
    @BeforeEach
    void setUp() {
        /* Como agora temos injeção por construtor, passamos os Mocks diretamente via 'new'.
         Isso garante que o Service está sendo criado de forma 100% controlada por nós, e não por "mágica" */
        this.bookService = new BookService(bookRepository, tagRepository, authorService);
    }

    // O nosso teste vira aqui dentro!
    @Test
    @DisplayName("Deve lançar exceção ao tentar aplicar desconto em um books que não é promotional")
    void shouldNotApplyDiscountOnPhysicalBook() {
        //  DADO (Arrange): Preparamos o cenário
        Long bookId = 1L;
        Book physicalBook = new Physical(); // Se sua classe se chamar Fisico, ajuste aqui
        physicalBook.setId(bookId);
        physicalBook.setPrice(BigDecimal.valueOf(100));

        DiscountRequestDTO dto = new DiscountRequestDTO(new BigDecimal("0.1")); // 0.1 em decimal (percentual).


        // MELHORIA: BDDMockito (given/willReturn) em vez de Mockito clássico (when/thenReturn)
        // Como você comenta "DADO" (Given), a sintaxe BDDMockito deixa a leitura do código mais natural.
        given(bookRepository.findById(bookId)).willReturn(Optional.of(physicalBook));

        //  QUANDO (Act) e 3. ENTÃO (Assert): Executamos e verificamos a explosão controlada
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> {
            bookService.applyDiscount(bookId, dto);
        });

        // Verificamos se a mensagem do erro e exatamente a que programamos no Service!
        assertEquals("Este books não é promotional e não aceita descontos.", error.getMessage());


    }

    @Test
    @DisplayName("Deve aplicar desconto corretamente em um Ebook")
    void shouldApplyDiscountOnEbook() {
        // 1. DADO (Arrange): Preparamos um Ebook de R$ 100,00
        Long bookId = 2L;
        // Ajuste para o nome exato da sua classe Ebook
        Book ebook = new Ebook();
        ebook.setId(bookId);
        ebook.setPrice(BigDecimal.valueOf(100));

        DiscountRequestDTO dto = new DiscountRequestDTO(BigDecimal.valueOf(0.1)); // 0.1 em decimal (percentual)

        // Treinamos o banco: "Ao buscar o ID 2, devolva o Ebook"
        given(bookRepository.findById(bookId)).willReturn(Optional.of(ebook));

        // Treinamos o banco: "Ao mandar salvar qualquer books, apenas devolva o proprio books salvo"
        // (Isso e necessario porque o Service chama o repository.save() no final do processo!)
        given(bookRepository.save(ebook)).willReturn(ebook);

        // 2. QUANDO (Act): O Gerente tenta aplicar o desconto
        Book updatedBook = bookService.applyDiscount(bookId, dto);

        // 3. ENTÃO (Assert): O preço deve ter caido para 90.0!
        // Solução: Usar compareTo(). Ele retorna 0 se os valores forem matematicamente iguais.
        assertEquals(0, BigDecimal.valueOf(90).compareTo(updatedBook.getPrice()),
                "O preço do Ebook deveria ter caído 10% (de 100 para 90)");
    }

    @Test
    @DisplayName("Deve instanciar um Ebook corretamente e associar as Tags")
    void shouldSaveEbookWithTags() {
        // 1. DADO (Arrange): Preparamos os objetos falsos
        Author fakeAuthor = new Author();
        fakeAuthor.setId(1L);
        fakeAuthor.setName("Tolkien");

        Tag fictionTag = new Tag(); fictionTag.setId(1L); fictionTag.setName("Ficção");
        Tag technologyTag = new Tag(); technologyTag.setId(2L); technologyTag.setName("Tecnologia");

        // Montamos o DTO como se viesse do Front-end (Ajuste os nulls conforme a ordem do seu Record)
        BookRequestDTO dto = new BookRequestDTO(
                Type.EBOOK, "O Silmarillion", 1L, BigDecimal.valueOf(500),
                500, Genre.FICCAO, LocalDate.of(1977,9,15 ), null, 10.5, 210,
                null,List.of(1L,2L)
        );

        /* Treinamos os nossos bancos de dados falsos (Mocks) para tagRespository.
        Treinamos o Mock do service, como geralmente um método 'buscaPorId' num Service já retorna a entidade
        ou lança exceção (diferente do repositório que retorna Optional), passamos o objeto direto. */
        given(authorService.findAuthorById(1L)).willReturn(fakeAuthor);
        given(tagRepository.findAllById(List.of(1L, 2L))).willReturn(List.of(fictionTag, technologyTag));


        // A mágica do given (any): "Quando mandarem salvar QUALQUER books, apenas devolva o proprio books salvo"
        given(bookRepository.save(any(Book.class))).willAnswer(invocation -> invocation.getArgument(0));

        // 2. QUANDO (Act): Chamamos o metodo do Service
        Book savedBook = bookService.saveBook(dto);

        // 3. ENTÃO (Assert): O grande tribunal do JUnit
        // Comprovamos que a Fábrica funcionou (Ele gerou a classe filha correta?)
        assertInstanceOf(Ebook.class, savedBook, "A fábrica deveria ter instanciado a classe Ebook");

        // Fazemos o "Cast" (Conversão) para Ebook para testar os campos específicos
        Ebook savedEbook = (Ebook) savedBook;
        assertEquals(10.5, savedEbook.getSizeMb(), "O tamanho em MB deveria ter sido preenchido");

        // Comprovamos que o N:N funcionou
        assertEquals(2, savedBook.getTags().size(), "O books deveria ter 2 tags anexadas");
        assertEquals("Ficção", savedBook.getTags().get(0).getName());
    }

}