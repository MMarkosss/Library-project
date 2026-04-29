package MarkLivraria.api.features.books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table(name = "fisico")
public class Physical extends Book{
    @Column(name = "peso_gramas")
    private Integer weightGrams;
    @Column (name = "quantidade_estoque")
    private Integer stockQuantity;

    // Construtor vazio obrigatório do JPA!
    public Physical () {super();}
}
