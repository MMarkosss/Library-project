package br.com.livraria.api_livraria.model;
import java.time.LocalDate;
import jakarta.persistence.*; // Ou javax.persistence em versões antigas
import java.lang.IllegalAccessException;

@Entity
@Table(name = "tb_livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;
    private int paginas;
    @Enumerated(EnumType.STRING)
    private Genero genero;
    private double preco;
    private LocalDate dataLancamento;


    public void setTitulo(String novoTitulo) {
        this.titulo = novoTitulo;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setAutor(String novoAutor) {
        this.autor = novoAutor;
    }

    public String getAutor() {
        return this.autor;
    }

    public void setPaginas(int novaPaginas) {
        if (novaPaginas <= 0) {
            throw new IllegalArgumentException("O número de páginas deve ser maior que zero!");
        }
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

    // ADICIONE ESTE MÉTODO:
    public void setId(Long id) {
        this.id = id;
    }
    // 1. CONSTRUTOR VAZIO (Obrigatório para o JPA funcionar)
    public Livro() { }
    // Construtor
    public Livro(String titulo, String autor, int paginas, Genero genero, double preco, LocalDate dataLancamento) {
        setTitulo(titulo);
        setAutor(autor);
        setPaginas(paginas);
        setGenero(genero);
        setPreco(preco);
        setDataLancamento(dataLancamento);

    }
}
