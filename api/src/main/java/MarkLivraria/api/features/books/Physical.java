package MarkLivraria.api.features.books;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table (name = "physical")
public class Physical extends Book{
    @Column(name = "weight_grams")
    private Integer weightGrams;
    @Column (name = "stock_quantity")
    private Integer stockQuantity;

    // Construtor vazio obrigatório do JPA!
    public Physical () {super();}
}
