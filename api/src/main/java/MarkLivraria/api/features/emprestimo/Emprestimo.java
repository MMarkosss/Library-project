package MarkLivraria.api.features.emprestimo;

import MarkLivraria.api.features.Livro.Livro;
import MarkLivraria.api.features.Usuario.Usuario;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id")
    private Livro livro;

    private LocalDateTime dataEmprestimo;
    private LocalDateTime dataDevolucaoPrevista;

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;
}