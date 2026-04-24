package MarkLivraria.api.features.Tag;

import MarkLivraria.api.features.Tag.dto.TagRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag buscaTagPorId (Long id) {
        return tagRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Tag> listaTags () {
        return tagRepository.findAll();
    }

    public Tag salvaTag (TagRequestDTO dto) {
        Tag tagSalva = new Tag();

        tagSalva.setNome(dto.nome());
        tagSalva.setDescricao(dto.descricao());

        return tagRepository.save(tagSalva);
    }

    public Tag atualizaTag (Long id, TagRequestDTO dto) {
        Tag tagExiste = buscaTagPorId(id);

        tagExiste.setNome(dto.nome());
        tagExiste.setDescricao(dto.descricao());

        return tagRepository.save(tagExiste);
    }

    public void deletaTag (Long id) {
        Tag tagExiste = buscaTagPorId(id);

        tagRepository.delete(tagExiste);
    }
}
