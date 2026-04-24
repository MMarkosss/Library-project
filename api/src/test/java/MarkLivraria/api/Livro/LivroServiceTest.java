package MarkLivraria.api.Livro;

import MarkLivraria.api.features.Autor.Autor;
import MarkLivraria.api.features.Autor.AutorService;
import MarkLivraria.api.features.Livro.*;
import MarkLivraria.api.features.Livro.dto.LivroRequestDTO;
import MarkLivraria.api.features.Promocional.dto.DescontoRequestDTO;
import MarkLivraria.api.features.Tag.Tag;
import MarkLivraria.api.features.Tag.TagRepository;
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
class LivroServiceTest {

    @Mock
    private  LivroRepository livroRepository; // O banco de dados FALSO
    @Mock
    private AutorService autorService;
    @Mock
    private TagRepository tagRepository;


    /* 1. MELHORIA: Removemos o @InjectMocks.
     O nosso Service agora será instanciado manualmente! */
    private LivroService livroService;

    // 2. MELHORIA: Setup explícito.
    // O JUnit roda este método antes de CADA UM dos @Test.
    @BeforeEach
    void setUp() {
        /* Como agora temos injeção por construtor, passamos os Mocks diretamente via 'new'.
         Isso garante que o Service está sendo criado de forma 100% controlada por nós, e não por "mágica" */
        this.livroService = new LivroService(livroRepository, tagRepository, autorService);
    }

    // O nosso teste vira aqui dentro!
    @Test
    @DisplayName("Deve lançar exceção ao tentar aplicar desconto em um livro que não é promocional")
    void naoDeveAplicarDescontoEmLivroFisico() {
        //  DADO (Arrange): Preparamos o cenário
        Long idLivro = 1L;
        Livro livroFisico = new Fisico(); // Se sua classe se chamar Fisico, ajuste aqui
        livroFisico.setId(idLivro);
        livroFisico.setPreco(BigDecimal.valueOf(100));

        DescontoRequestDTO dto = new DescontoRequestDTO(new BigDecimal("0.1")); // 0.1 em decimal (percentual).


        // MELHORIA: BDDMockito (given/willReturn) em vez de Mockito clássico (when/thenReturn)
        // Como você comenta "DADO" (Given), a sintaxe BDDMockito deixa a leitura do código mais natural.
        given(livroRepository.findById(idLivro)).willReturn(Optional.of(livroFisico));

        //  QUANDO (Act) e 3. ENTÃO (Assert): Executamos e verificamos a explosão controlada
        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class, () -> {
            livroService.aplicarDesconto(idLivro, dto);
        });

        // Verificamos se a mensagem do erro e exatamente a que programamos no Service!
        assertEquals("Este livro não é promocional e não aceita descontos.", erro.getMessage());


    }

    @Test
    @DisplayName("Deve aplicar desconto corretamente em um Ebook")
    void deveAplicarDescontoEmEbook() {
        // 1. DADO (Arrange): Preparamos um Ebook de R$ 100,00
        Long idLivro = 2L;
        // Ajuste para o nome exato da sua classe Ebook
        Livro ebook = new Ebook();
        ebook.setId(idLivro);
        ebook.setPreco(BigDecimal.valueOf(100));

        DescontoRequestDTO dto = new DescontoRequestDTO(BigDecimal.valueOf(0.1)); // 0.1 em decimal (percentual)

        // Treinamos o banco: "Ao buscar o ID 2, devolva o Ebook"
        given(livroRepository.findById(idLivro)).willReturn(Optional.of(ebook));

        // Treinamos o banco: "Ao mandar salvar qualquer livro, apenas devolva o proprio livro salvo"
        // (Isso e necessario porque o Service chama o repository.save() no final do processo!)
        given(livroRepository.save(ebook)).willReturn(ebook);

        // 2. QUANDO (Act): O Gerente tenta aplicar o desconto
        Livro livroAtualizado = livroService.aplicarDesconto(idLivro, dto);

        // 3. ENTÃO (Assert): O preço deve ter caido para 90.0!
        // Solução: Usar compareTo(). Ele retorna 0 se os valores forem matematicamente iguais.
        assertEquals(0, BigDecimal.valueOf(90).compareTo(livroAtualizado.getPreco()),
                "O preço do Ebook deveria ter caído 10% (de 100 para 90)");
    }

    @Test
    @DisplayName("Deve instanciar um Ebook corretamente e associar as Tags")
    void deveSalvarEbookComTags() {
        // 1. DADO (Arrange): Preparamos os objetos falsos
        Autor autorFalso = new Autor();
        autorFalso.setId(1L);
        autorFalso.setNome("Tolkien");

        Tag tagFiccao = new Tag(); tagFiccao.setId(1L); tagFiccao.setNome("Ficção");
        Tag tagTecnologia = new Tag(); tagTecnologia.setId(2L); tagTecnologia.setNome("Tecnologia");

        // Montamos o DTO como se viesse do Front-end (Ajuste os nulls conforme a ordem do seu Record)
        LivroRequestDTO dto = new LivroRequestDTO(
                Tipo.EBOOK, "O Silmarillion", 1L, BigDecimal.valueOf(500),
                500, Genero.FICCAO, LocalDate.of(1977,9,15 ), null, 10.5, 210,
                null,List.of(1L,2L)
        );

        /* Treinamos os nossos bancos de dados falsos (Mocks) para tagRespository.
        Treinamos o Mock do service, como geralmente um método 'buscaPorId' num Service já retorna a entidade
        ou lança exceção (diferente do repositório que retorna Optional), passamos o objeto direto. */
        given(autorService.buscaAutorPorId(1L)).willReturn(autorFalso);
        given(tagRepository.findAllById(List.of(1L, 2L))).willReturn(List.of(tagFiccao, tagTecnologia));


        // A mágica do given (any): "Quando mandarem salvar QUALQUER livro, apenas devolva o proprio livro salvo"
        given(livroRepository.save(any(Livro.class))).willAnswer(invocacao -> invocacao.getArgument(0));

        // 2. QUANDO (Act): Chamamos o metodo do Service
        Livro livroSalvo = livroService.salvarLivro(dto);

        // 3. ENTÃO (Assert): O grande tribunal do JUnit
        // Comprovamos que a Fábrica funcionou (Ele gerou a classe filha correta?)
        assertInstanceOf(Ebook.class, livroSalvo, "A fábrica deveria ter instanciado a classe Ebook");

        // Fazemos o "Cast" (Conversão) para Ebook para testar os campos específicos
        Ebook ebookSalvo = (Ebook) livroSalvo;
        assertEquals(10.5, ebookSalvo.getTamanhoMB(), "O tamanho em MB deveria ter sido preenchido");

        // Comprovamos que o N:N funcionou
        assertEquals(2, livroSalvo.getTags().size(), "O livro deveria ter 2 tags anexadas");
        assertEquals("Ficção", livroSalvo.getTags().get(0).getNome());
    }

}
