package MarkLivraria.api.features.tags;

import org.springframework.data.jpa.domain.Specification;

public class TagSpecs {
    public static Specification<Tag> nameContains (String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%" + name.toLowerCase() + "%");
    }
}