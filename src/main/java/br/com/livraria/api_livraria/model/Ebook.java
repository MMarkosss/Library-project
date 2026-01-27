package br.com.livraria.api_livraria.model;

import java.time.LocalDate;

public class Ebook extends Livro implements Promocional {
    private String marcaDagua;


    public Ebook() {
        super(); // Chama o construtor vazio da classe Livro
    }

    public Ebook(String titulo, String autor, int paginas, Genero genero, double preco, LocalDate dataLancamento, String marcaDagua, double porcentagem) {
        super(titulo, autor, paginas, genero, preco, dataLancamento);
        this.setmarcaDagua(marcaDagua);
        this.aplicarDesconto(porcentagem);
    }


    public void setmarcaDagua(String marcaDagua) {
        this.marcaDagua = marcaDagua;
    }

    public String getmarcaDagua() {
        return marcaDagua;
    }


    @Override
    public boolean aplicarDesconto(double porcentagem) {
        if (porcentagem < 30 && porcentagem>=0) {
            double desconto = getPreco() * (porcentagem / 100);
            setPreco(getPreco() - desconto);
            return true;
        } else {
            return false;
        }
    }
}
