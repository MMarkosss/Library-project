package MarkLivraria.api.features.Autenticacao;

import MarkLivraria.api.features.Usuario.Usuario;
import MarkLivraria.api.infra.security.services.TokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AutenticacaoController {

    private final AuthenticationManager manager;
    private final TokenService tokenService; // Injetamos a nossa fábrica de tokens!

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);

        // 1. Recuperamos o utilizador que acabou de ser validado na base de dados
        var usuario = (Usuario) authentication.getPrincipal();

        // 2. Fabricamos o Token JWT para este utilizador
        var tokenJWT = tokenService.gerarToken(usuario);

        // 3. Devolvemos o Token formatado no nosso DTO
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
