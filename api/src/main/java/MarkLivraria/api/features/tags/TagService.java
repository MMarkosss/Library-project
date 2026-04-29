package MarkLivraria.api.features.tags;

import MarkLivraria.api.features.tags.dto.TagRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag findTagById (Long id) {
        return tagRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Tag> listTags(String name) {
        List<Specification<Tag>> specs = new ArrayList<>();

        if (StringUtils.hasText(name)) {
            specs.add(TagSpecs.nameContains(name));
        }

        // StringUtils: Verifica se a String não é nula, não é vazia e contém texto real
        // Para escalar, basta adicionar novos filtros à lista 'specs' aqui
        // if (status != null) specs.add(TagSpecs.temStatus(status));

        // Specification.allOf resolve o problema do null pois retorna uma
        // Specification válida (conjunction) mesmo para listas vazias.
        return tagRepository.findAll(Specification.allOf(specs), Sort.unsorted());
    }


    public Tag saveTag (TagRequestDTO dto) {
        Tag savedTag = new Tag();

        savedTag.setName(dto.name());
        savedTag.setDescription(dto.description());

        return tagRepository.save(savedTag);
    }

    public Tag updateTag (Long id, TagRequestDTO dto) {
        Tag existingTag = findTagById(id);

        existingTag.setName(dto.name());
        existingTag.setDescription(dto.description());

        return tagRepository.save(existingTag);
    }

    public void deleteTag (Long id) {
        Tag existingTag = findTagById(id);

        tagRepository.delete(existingTag);
    }
}