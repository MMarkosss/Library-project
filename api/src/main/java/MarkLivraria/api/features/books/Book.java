package MarkLivraria.api.features.books;

import MarkLivraria.api.features.authors.Author;
import MarkLivraria.api.features.tags.Tag;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table(name = "books")
@Inheritance(strategy = InheritanceType.JOINED) // O Hibernate agora sabe que deve quebrar as tabelas!
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne // 1. Diz ao Spring: "Muitos livros para Um authors"
    @JoinColumn(name = "autor_id",nullable = false) // 2. Cria a coluna de Chave Estrangeira no banco
    private Author author;

    private Integer pages;

    @ManyToMany
    @JoinTable(
            name = "livro_tag", // O nome da tabela intermediária lá do V2
            joinColumns = @JoinColumn(name = "livro_id"), // O id do dono do relacionamento (Livro)
            inverseJoinColumns = @JoinColumn(name = "tag_id") // O id do outro lado (Tag)
    )
    private List<Tag> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Genre genre;


    private BigDecimal price;

    private LocalDate publicationDate;


    // 1. CONSTRUTOR VAZIO (Obrigatório para o JPA funcionar)
    public Book() {}
}
