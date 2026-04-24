package MarkLivraria.api.features.Usuario;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Entity
@Table (name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String login;

    @Column(nullable = false)
    private String senha;

    // Adicionamos a coluna do cargo no banco
    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Role role;

    // Construtor vazio obrigatório do JPA!
    public Usuario() {}

    // =======================================================================
    // MÉTODOS OBRIGATÓRIOS DO CONTRATO 'UserDetails' DO SPRING SECURITY
    // =======================================================================

    // 1. Quais são os cargos (Roles) desse usuário? (Vamos deixar um cargo padrão)
    // Ensinamos o Spring a ler o nosso cargo!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // ATENÇÃO: O Spring Security exige por padrão que os cargos tenham o prefixo "ROLE_"
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // 2. O Spring precisa saber qual campo é a senha
    @Override
    public String getPassword() {
        return senha;
    }

    // 3. O Spring precisa saber qual campo é o login principal
    @Override
    public String getUsername() {
        return login;
    }

    // 4. As 4 verificações abaixo checam se a conta está bloqueada, expirada, etc.
    // Como não vamos usar isso agora, retornamos 'true' (tudo liberado) para todas!
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
