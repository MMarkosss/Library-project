package MarkLivraria.api.features.Livro;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table(name = "fisico")
public class Fisico extends  Livro{
    @Column(name = "peso_gramas")
    private Integer pesoGramas;
    @Column (name = "quantidade_estoque")
    private Integer quantidadeEstoque;

    // Construtor vazio obrigatório do JPA!
    public Fisico () {super();}
}
