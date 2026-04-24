package MarkLivraria.api.features.Usuario;

import MarkLivraria.api.features.Usuario.dto.UsuarioRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public void registrarUsuario(UsuarioRequestDTO dto) {

        // 1. Verifica se o e-mail já existe no banco
        if (usuarioRepository.findByLogin(dto.login()) != null) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        // 2. Instancia o usuário e faz o Hash da senha!
        var novoUsuario = new Usuario();
        novoUsuario.setLogin(dto.login());
        novoUsuario.setSenha(passwordEncoder.encode(dto.senha())); // MÁGICA AQUI!

        // 3. Força o perfil para USER (Segurança: Ninguém pode se cadastrar como ADMIN)
        novoUsuario.setRole(Role.USER);

        // 4. Salva no banco
        usuarioRepository.save(novoUsuario);
    }
}
