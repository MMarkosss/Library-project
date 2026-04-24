package MarkLivraria.api.features.Livro;

import MarkLivraria.api.features.Promocional.Promocional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity // 1. Sim, a classe filha também precisa ser uma entidade!
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
public class Ebook extends Livro implements Promocional {
    @Column(name = "tamanho_mb")
    private Double tamanhoMB;
    @Column(name = "marca_dagua")
    private String marcaDagua;

    // Chama o construtor vazio da classe Livro
    public Ebook() {
        super();
    }

    @Override
    public boolean aplicarDescontoDe (BigDecimal percentual) {
        boolean maiorQZero = percentual.compareTo(BigDecimal.valueOf(0))>0;
        boolean menorQTrinta = percentual.compareTo(BigDecimal.valueOf(0.3))<0;
        if (maiorQZero && menorQTrinta) {
            BigDecimal desconto = getPreco().multiply(percentual);
            setPreco(getPreco().subtract(desconto));
            return true;
        }
        return false;
    }
}
