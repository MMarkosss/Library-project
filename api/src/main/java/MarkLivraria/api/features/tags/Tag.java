package MarkLivraria.api.features.tags;

import MarkLivraria.api.features.books.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table (name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    // Relacionamento reverso: A Tag também sabe em quais livros ela está
    @ManyToMany(mappedBy = "tags")
    private List<Book> books = new ArrayList<>();

    // Construtor vazio obrigatório do JPA!
    public Tag() {}
}
