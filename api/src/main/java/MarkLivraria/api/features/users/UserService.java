package MarkLivraria.api.features.users;

import MarkLivraria.api.features.users.dto.UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRequestDTO dto) {

        // 1. Verifica se o e-mail já existe no banco
        if (userRepository.findByLogin(dto.login()) != null) {
            throw new IllegalArgumentException("Este e-mail já está em uso.");
        }

        // 2. Instancia o usuário e faz o Hash da senha!
        var newUser = new User();
        newUser.setLogin(dto.login());
        newUser.setPassword(passwordEncoder.encode(dto.password())); // MÁGICA AQUI!

        // 3. Força o perfil para USER (Segurança: Ninguém pode se cadastrar como ADMIN)
        newUser.setRole(Role.USER);

        // 4. Salva no banco
        userRepository.save(newUser);
    }
}
