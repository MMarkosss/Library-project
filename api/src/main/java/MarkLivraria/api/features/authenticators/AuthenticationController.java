package MarkLivraria.api.features.authenticators;

import MarkLivraria.api.features.users.User;
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
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService; // Injetamos a nossa fábrica de tokens!

    @PostMapping
    public ResponseEntity performLogin(@RequestBody @Valid AuthenticationData data) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var authentication = manager.authenticate(authenticationToken);

        // 1. Recuperamos o utilizador que acabou de ser validado na base de dados
        var user = (User) authentication.getPrincipal();

        // 2. Fabricamos o Token JWT para este utilizador
        var jwtToken = tokenService.generateToken(user);

        // 3. Devolvemos o Token formatado no nosso DTO
        return ResponseEntity.ok(new JwtTokenData(jwtToken));
    }
}
