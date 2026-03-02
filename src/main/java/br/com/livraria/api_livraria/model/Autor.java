package br.com.livraria.api_livraria.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
//importe para falar que column é definido not null no banco (db).
import jakarta.persistence.Column;
//Importando list e arraylist para "autor descrever seus livros" e anotação ManyToOne com parametro mapped.
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;




@Entity
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

    // ... gere os Getters e Setters para id, nome e biografia

    public void setId (Long id) {
        this.id=id;
    }
    public Long getId() {
        return this.id;
    }

    public void setNome (String nome) {
        this.nome=nome;
    }
    public String getNome () {
        return this.nome;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setLivros (Livro livro) {
        livros.add(livro);
    }

    public List<Livro> getLivros () {
        return this.livros;
    }

}