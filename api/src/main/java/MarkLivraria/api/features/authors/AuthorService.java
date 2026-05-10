package MarkLivraria.api.features.authors;

import MarkLivraria.api.features.authors.dto.AuthorRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;


    public Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public Author saveAuthor (AuthorRequestDTO dto) {
        Author savedAuthor = new Author();

        savedAuthor.setName(dto.name());
        savedAuthor.setBiography(dto.biography());

        return authorRepository.save(savedAuthor);
    }

    public Page<Author> listAuthors (String name, String bookTitle, Pageable pageable) {
        List <Specification<Author>> specs = new ArrayList<>();

        if (name!=null && !name.isBlank()) {
            specs.add(AuthorSpecs.nameContains(name));
        }

        if (bookTitle!=null && !bookTitle.isBlank()) {
            specs.add(AuthorSpecs.bookTitleContains(bookTitle));
        }

        Specification <Author> finalSpec = specs.stream().reduce(Specification::and).orElse(null);

        return authorRepository.findAll(finalSpec, pageable);
    }

    public Author updateAuthor (Long id, AuthorRequestDTO dto) {
        Author existingAuthor = findAuthorById(id);

        existingAuthor.setName(dto.name());
        existingAuthor.setBiography(dto.biography());

        return authorRepository.save(existingAuthor);
    }

    public Author partialUpdate(Long id, Map<String, Object> fields) {

        Author existingAuthor = findAuthorById(id); // Usa o seu método que lança exceção se não achar

        // Instancia manualmente e registra os módulos nativos (essencial para ler LocalDate)
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        // Iteramos sobre o Map (Chave = nome do campo no JSON, Valor = conteúdo)
        fields.forEach((propertyName, propertyValue) -> {

            // 1. Procura se existe um atributo na classe Author com o exato nome da chave
            Field field = ReflectionUtils.findField(Author.class, propertyName);

            if (field != null) {
                // 2. Quebra o encapsulamento do Java (pois o atributo é private)
                field.setAccessible(true);

                // 3. Converte o valor que veio do JSON para o tipo correto que o Java espera
                // (Ex: Converte a String "1990-01-01" para LocalDate, se houver esse campo)
                Object newValue = objectMapper.convertValue(propertyValue, field.getType());

                // 4. Injeta o novo valor dinamicamente no objeto
                ReflectionUtils.setField(field, existingAuthor, newValue);
            }
        });

        return authorRepository.save(existingAuthor);
    }

    public void deleteAuthor (Long id) {
        Author existingAuthor = findAuthorById(id);

        authorRepository.delete(existingAuthor);
    }
}
