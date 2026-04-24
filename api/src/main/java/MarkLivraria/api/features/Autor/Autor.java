package MarkLivraria.api.features.Autor;

import MarkLivraria.api.features.Livro.Livro;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) // Avisa o Hibernate: "Fique tranquilo, lá no banco isso é NOT NULL"
    private String nome;

    @OneToMany(mappedBy = "autor") // O nome "autor" tem que ser IGUAL ao nome da variável que está na classe Livro
    private List<Livro> livros = new ArrayList<>();

    private String biografia;

    // Construtor vazio obrigatório do JPA!
    public Autor() {
    }

}
