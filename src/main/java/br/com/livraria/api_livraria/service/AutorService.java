package br.com.livraria.api_livraria.service;

import br.com.livraria.api_livraria.model.Autor;
import br.com.livraria.api_livraria.repository.AutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutorService {

    @Autowired
    private AutorRepository repository;

    public Autor buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
    }
}