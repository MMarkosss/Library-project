package br.com.livraria.api_livraria.model;
import java.time.LocalDate;
import jakarta.persistence.*; // Ou javax.persistence em versões antigas
// Importe das classes valid:
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
// Importe estas duas classes(heranca):
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorValue;
// Importe estas duas classes do Jackson(heranca):
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
//Importe de tabelas relacionais
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
//importe para falar que column é definido not null no banco (db).
import jakarta.persistence.Column;

// 1. Diz ao Spring: "Olhe para uma propriedade chamada 'tipo' no JSON"
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipo")
// 2. Mapeia os valores dessa propriedade para as classes corretas:
@JsonSubTypes({
        @JsonSubTypes.Type(value = Livro.class, name = "FISICO"),
        @JsonSubTypes.Type(value = Ebook.class, name = "EBOOK")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 1. Estratégia de Tabela Única
@DiscriminatorColumn(name = "tipo_livro") // 2. O nome da coluna mágica
@DiscriminatorValue("FISICO") // 3. Se for um livro normal, a coluna terá a palavra "FISICO"
@Table(name = "tb_livros")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ManyToOne // 1. Diz ao Spring: "Muitos livros para Um autor"
    @JoinColumn(name = "autor_id",nullable = false) // 2. Cria a coluna de Chave Estrangeira no banco
    private Autor autor;

    private int paginas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genero genero;

    // Obs: Se 'paginas' for do tipo primitivo 'int' e 'preco' for 'double',
    // o Java já sabe que eles não aceitam nulos, então geralmente não dão esse erro.

    private double preco;

    private LocalDate dataLancamento;

    public void setTitulo(String novoTitulo) {
        this.titulo = novoTitulo;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setAutor(Autor novoAutor) {
        this.autor = novoAutor;
    }

    public Autor getAutor() {
        return this.autor;
    }

    public void setPaginas(int novaPaginas) {
       /*
       A regra de negocio fica a cargo do valid no spring.
       if (novaPaginas <= 0) {
            throw new IllegalArgumentException("O número de páginas deve ser maior que zero!");
        } */
        this.paginas = novaPaginas;

    }

    public int getPaginas() {
        return paginas;
    }

    public void setGenero(Genero novoGenero) {
        this.genero = novoGenero;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setPreco(double novoPreco) {
        this.preco = novoPreco;
    }

    public double getPreco() {
        return preco;
    }

    public void setDataLancamento(LocalDate data) {
        this.dataLancamento = data;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    // 3. GETTER e SETTER DO ID (Para o JSON conseguir ler o ID gerado e o put funcionar)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // 1. CONSTRUTOR VAZIO (Obrigatório para o JPA funcionar)
    public Livro() { }
    // Construtor
    public Livro(String titulo, Autor autor, int paginas, Genero genero, double preco, LocalDate dataLancamento) {
        setTitulo(titulo);
        setAutor(autor);
        setPaginas(paginas);
        setGenero(genero);
        setPreco(preco);
        setDataLancamento(dataLancamento);

    }
}
