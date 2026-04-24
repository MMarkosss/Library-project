package MarkLivraria.api.features.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // O retorno precisa ser UserDetails para integrar facilmente com o Security
    // O Spring Data JPA cria o SQL automaticamente lendo o nome do método!
    UserDetails findByLogin(String login);
}