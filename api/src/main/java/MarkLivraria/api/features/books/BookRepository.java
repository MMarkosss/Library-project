package MarkLivraria.api.features.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


// Só isso. O Spring cria o SQL sozinho.
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    // Não é mais necessário declarar os métodos findBy... aqui
}