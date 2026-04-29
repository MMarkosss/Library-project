package MarkLivraria.api.features.books;

import org.springframework.data.jpa.domain.Specification;

public class BookSpecs {

    public static Specification<Book> titleContains(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
    }

    public static Specification<Book> genreEquals(Genre genre) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("genre"), genre);
    }
}
