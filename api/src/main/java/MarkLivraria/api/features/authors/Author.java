package MarkLivraria.api.features.authors;

import MarkLivraria.api.features.books.Book;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table (name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Avisa o Hibernate: "Fique tranquilo, lá no banco isso é NOT NULL"
    private String name;

    @OneToMany(mappedBy = "author") // O nome "authors" tem que ser IGUAL ao nome da variável que está na classe Livro
    private List<Book> books = new ArrayList<>();

    private String biography;

    // Construtor vazio obrigatório do JPA!
    public Author() {
    }

}
