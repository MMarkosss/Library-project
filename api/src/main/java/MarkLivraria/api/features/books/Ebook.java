package MarkLivraria.api.features.books;

import MarkLivraria.api.features.promotional.Promotional;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity // 1. Sim, a classe filha também precisa ser uma entidade!
@Getter // Cria todos os get() invisivelmente
@Setter // Cria todos os set() invisivelmente
@Table (name = "ebooks")
public class Ebook extends Book implements Promotional {
    @Column(name = "size_mb")
    private Double sizeMb;
    @Column(name = "watermark")
    private String watermark;

    // Chama o construtor vazio da classe Livro
    public Ebook() {
        super();
    }

    @Override
    public boolean applyDiscountOf (BigDecimal percentage) {
        boolean greaterThanZero = percentage.compareTo(BigDecimal.valueOf(0))>0;
        boolean lessThanThirty = percentage.compareTo(BigDecimal.valueOf(0.3))<0;
        if (greaterThanZero && lessThanThirty) {
            BigDecimal discount = getPrice().multiply(percentage);
            setPrice(getPrice().subtract(discount));
            return true;
        }
        return false;
    }
}
