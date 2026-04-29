package MarkLivraria.api.features.authors;

import MarkLivraria.api.features.books.Book;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecs {

    public static Specification<Author> nameContains(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Author> bookTitleContains(String bookTitle) {
        return (root, query, criteriaBuilder) -> {
            // 1. Aplica o DISTINCT para evitar autores repetidos nos resultados
            query.distinct(true);

            // 2. Faz o JOIN com a lista de livros (equivalente ao JOIN a.livros l no JPQL)
            Join<Author, Book> booksJoin = root.join("books");

            // 3. Aplica a condição do LIKE na tabela joinada
            return criteriaBuilder.like(
                    criteriaBuilder.lower(booksJoin.get("title")),
                    "%" + bookTitle.toLowerCase() + "%"
            );
        };
    }
}
