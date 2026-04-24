package MarkLivraria.api.features.Autor;

import MarkLivraria.api.features.Autor.dto.AutorRequestDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorService {

    private final AutorRepository autorRepository;

    public Autor buscaAutorPorId(Long id) {
        return autorRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public List<Autor> listarAutores() {
        return autorRepository.findAll();
    }

    public Autor salvarAutor (AutorRequestDTO dto) {
        Autor autorSalvo = new Autor();

        autorSalvo.setNome(dto.nome());
        autorSalvo.setBiografia(dto.biografia());

        return autorRepository.save(autorSalvo);
    }

    public Autor atualizaAutor (Long id,AutorRequestDTO dto) {
        Autor autorExiste = buscaAutorPorId(id);

        autorExiste.setNome(dto.nome());
        autorExiste.setBiografia(dto.biografia());

        return autorRepository.save(autorExiste);
    }

    public void deletaAutor (Long id) {
        Autor autorExiste = buscaAutorPorId(id);

        autorRepository.delete(autorExiste);
    }
}
