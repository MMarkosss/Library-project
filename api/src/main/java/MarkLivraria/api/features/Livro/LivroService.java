package MarkLivraria.api.features.Livro;


import MarkLivraria.api.features.Autor.Autor;
import MarkLivraria.api.features.Autor.AutorService;
import MarkLivraria.api.features.Livro.dto.LivroRequestDTO;
import MarkLivraria.api.features.Promocional.dto.DescontoRequestDTO;
import MarkLivraria.api.features.Promocional.Promocional;
import MarkLivraria.api.features.Tag.Tag;
import MarkLivraria.api.features.Tag.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LivroService {
    private final LivroRepository livroRepository;
    private final TagRepository tagRepository;
    private final AutorService autorService;

    public Page<Livro> pagesLivros (Pageable page) {
        return livroRepository.findAll(page);
    }


    public Livro buscaLivroPorId(Long id) {
        return livroRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    public void preencheCamposEmComumLivro(Livro livroPreenchido, LivroRequestDTO dto) {

        Autor autor = autorService.buscaAutorPorId(dto.autorId());
        livroPreenchido.setTitulo(dto.titulo());
        livroPreenchido.setAutor(autor);
        livroPreenchido.setPreco(dto.preco());
        livroPreenchido.setGenero(dto.genero());
        livroPreenchido.setDataPublicacao(dto.dataPublicacao());
        livroPreenchido.setPaginas(dto.paginas());


        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            List<Tag> tagsEncontradas = tagRepository.findAllById(dto.tagIds());
            livroPreenchido.setTags(tagsEncontradas);
        }
    }


    public Livro salvarLivro(LivroRequestDTO dto) {
        Livro novoLivro = switch (dto.tipo()) {
            case Tipo.EBOOK -> {
                Ebook ebook = new Ebook();
                // Validação manual rápida da regra de negócio:
                if (dto.tamanhoMb() == null) {
                    throw new IllegalArgumentException("O ebook precisa de ter um tamanho em MB");
                }
                ebook.setTamanhoMB(dto.tamanhoMb());
                ebook.setMarcaDagua(dto.marcaDagua());
                yield ebook; // Retorna a instância para a variável 'novoLivro'
            }
            case Tipo.FISICO -> {
                Fisico fisico = new Fisico();
                if (dto.pesoGramas() == null || dto.quantidade() == null || dto.quantidade() <= 0) {
                    throw new IllegalArgumentException("O livro físico precisa ter peso e uma quantidade válida maior que zero");
                }
                fisico.setPesoGramas(dto.pesoGramas());
                fisico.setQuantidadeEstoque(dto.quantidade());
                yield fisico;
            }
        };
        preencheCamposEmComumLivro(novoLivro,dto);

        return livroRepository.save(novoLivro);
    }

    public Livro atualizarLivro(Long id, LivroRequestDTO dto) {
        // 1. Busca o livro e o autor corretos
        Livro livroExistente = buscaLivroPorId(id);

        // 2. Trava de Segurança: Impede a mudança do DNA do livro
        boolean tentandoMudarTipo =
                (livroExistente instanceof Fisico && dto.tipo() == Tipo.EBOOK) ||
                        (livroExistente instanceof Ebook && dto.tipo() == Tipo.FISICO);

        if (tentandoMudarTipo) {
            throw new IllegalArgumentException("Não é possível alterar o formato do livro (Físico/Ebook) após o cadastro. Cadastre um novo livro.");
        }

        // 3. Atualiza os campos específicos das classes filhas
        if (livroExistente instanceof Fisico fisico) {
            if (dto.pesoGramas() == null || dto.quantidade() == null || dto.quantidade() < 0) {
                throw new IllegalArgumentException("O livro físico precisa ter peso e uma quantidade válida (maior ou igual a zero).");
            }
            fisico.setPesoGramas(dto.pesoGramas());
            fisico.setQuantidadeEstoque(dto.quantidade()); // Agora o estoque atualiza perfeitamente!

        } else if (livroExistente instanceof Ebook ebook) {
            if (dto.tamanhoMb() == null) {
                throw new IllegalArgumentException("O ebook precisa de ter um tamanho em MB.");
            }
            ebook.setTamanhoMB(dto.tamanhoMb());
            ebook.setMarcaDagua(dto.marcaDagua());
        }

        preencheCamposEmComumLivro(livroExistente,dto);

        return livroRepository.save(livroExistente);
    }

    public Livro atualizarParcial(Long id, LivroRequestDTO dto) {
        Livro livroExistente = buscaLivroPorId(id);

        // 1. Trava de Segurança do Tipo (Só avalia se o Front-end mandou o tipo no JSON)

        boolean tentandoMudarTipo =
                    (livroExistente instanceof Fisico && dto.tipo() == Tipo.EBOOK) ||
                            (livroExistente instanceof Ebook && dto.tipo() == Tipo.FISICO);

        if (tentandoMudarTipo) {
                throw new IllegalArgumentException("Não é possível alterar o formato do livro via PATCH, crie um novo livro.");
        }


        // 2. Atualiza os campos específicos (Filhos) apenas se não forem nulos
        if (livroExistente instanceof Fisico fisico) {
            if (dto.pesoGramas() != null) {
                fisico.setPesoGramas(dto.pesoGramas());
            }
            if (dto.quantidade() != null) {
                if (dto.quantidade() < 0) {
                    throw new IllegalArgumentException("A quantidade não pode ser negativa.");
                }
                fisico.setQuantidadeEstoque(dto.quantidade());
            }

        } else if (livroExistente instanceof Ebook ebook) {
            if (dto.tamanhoMb() != null) {
                ebook.setTamanhoMB(dto.tamanhoMb());
            }
            if (dto.marcaDagua() != null) {
                ebook.setMarcaDagua(dto.marcaDagua());
            }
        }

        // 3. Atualiza os dados gerais (Pai) apenas se não forem nulos
        if (dto.titulo() != null) livroExistente.setTitulo(dto.titulo());
        if (dto.preco() != null) livroExistente.setPreco(dto.preco());
        if (dto.paginas() != null) livroExistente.setPaginas(dto.paginas());
        if (dto.genero() != null) livroExistente.setGenero(dto.genero());
        if (dto.dataPublicacao() != null) livroExistente.setDataPublicacao(dto.dataPublicacao());

        // 4. Se mandou um novo Autor, busca e atualiza
        if (dto.autorId() != null) {
            Autor autor = autorService.buscaAutorPorId(dto.autorId());
            livroExistente.setAutor(autor);
        }

        // 5. Se mandou novas Tags, atualiza
        if (dto.tagIds() != null && !dto.tagIds().isEmpty()) {
            List<Tag> tagsEncontradas = tagRepository.findAllById(dto.tagIds());
            livroExistente.setTags(tagsEncontradas);
        }

        return livroRepository.save(livroExistente);
    }

    public void deletarLivro(Long id) {
        Livro livroExiste = buscaLivroPorId(id);
        livroRepository.delete(livroExiste);
    }

    public Livro aplicarDesconto(Long id, DescontoRequestDTO dto) {
        // 1. Busca o livro no banco
        Livro livro = livroRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        // 2. A MÁGICA: Verifica se a instância desse livro assinou o contrato Promocional
        if (livro instanceof Promocional promocional) {

            // 3. Tenta aplicar o desconto.
            boolean descontoAplicado = promocional.aplicarDescontoDe(dto.percentual());

            if (!descontoAplicado) {
                // Se a interface barrou (ex: desconto maior que o limite permitido)
                throw new IllegalArgumentException("O valor do desconto excedeu o limite permitido para este tipo de livro.");
            }

            // Se deu tudo certo, salva o livro com o novo preço no banco
            return livroRepository.save(livro);

        } else {
            // Se o livro não implementa a interface Promocional
            throw new IllegalArgumentException("Este livro não é promocional e não aceita descontos.");
        }
    }
}
