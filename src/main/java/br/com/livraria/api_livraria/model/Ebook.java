package br.com.livraria.api_livraria.model;

import java.time.LocalDate;
//import de discriminacao de heranca
import jakarta.persistence.Entity;
import jakarta.persistence.DiscriminatorValue;

@Entity // 1. Sim, a classe filha também precisa ser uma entidade!
@DiscriminatorValue("EBOOK") // 2. Quando salvarmos um Ebook, a coluna mágica receberá a palavra "EBOOK"
public class Ebook extends Livro implements Promocional {
    private String marcaDagua;


    public Ebook() {
        super(); // Chama o construtor vazio da classe Livro
    }

    public Ebook(String titulo, String autor, int paginas, Genero genero, double preco, LocalDate dataLancamento, String marcaDagua) {
        super(titulo, autor, paginas, genero, preco, dataLancamento);
        this.setmarcaDagua(marcaDagua);
    }


    public void setmarcaDagua(String marcaDagua) {
        this.marcaDagua = marcaDagua;
    }

    public String getmarcaDagua() {
        return marcaDagua;
    }


    @Override
    public boolean aplicarDesconto(Double porcentagem) {
        if (porcentagem < 30 && porcentagem>=0) {
            double desconto = getPreco() * (porcentagem / 100);
            setPreco(getPreco() - desconto);
            return true;
        } else {
            return false;
        }
    }
}
