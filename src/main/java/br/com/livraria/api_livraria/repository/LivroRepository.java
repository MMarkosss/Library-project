package br.com.livraria.api_livraria.repository;

import br.com.livraria.api_livraria.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    // Só isso. O Spring cria o SQL sozinho.
}