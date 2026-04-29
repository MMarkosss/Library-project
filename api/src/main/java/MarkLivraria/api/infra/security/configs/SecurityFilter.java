package MarkLivraria.api.infra.security.configs;

import MarkLivraria.api.features.users.UserRepository;
import MarkLivraria.api.infra.security.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component // Diz ao Spring para carregar essa classe na memória (como um componente genérico)
public class SecurityFilter extends OncePerRequestFilter { // Garante que o filtro rode apenas 1 vez por requisição


    private final TokenService tokenService;
    private final UserRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        // 1. Pega o token do cabeçalho da requisição
        var jwtToken = recoverToken(request);


        // 2. Se a pessoa mandou um token, vamos validá-lo!
        if (jwtToken != null) {
            try {
                // Descriptografa e pega o e-mail
                var subject = tokenService.getSubject(jwtToken);

                // Busca o usuário no banco para garantir que ele ainda existe e para saber os cargos (user,admin).
                var user = repository.findByLogin(subject);

                // Cria o "Crachá" de autorização oficial do Spring Security
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                // Força o Spring a fazer o login automático do usuário para essa requisição específica
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (RuntimeException ex) {
                /* Se o token estiver expirado ou alterado, cai aqui.
                 Não fazemos nada! O usuário simplesmente não será autenticado.
                 O Spring Security vai perceber que ele é "anônimo" e bloquear a rota automaticamente.*/
            }
        }

        // 3. Independentemente de ter token ou não, manda a requisição seguir o fluxo normal
        // Se ela não tiver token e tentar acessar uma rota bloqueada, o Spring barra lá na frente.
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para limpar a palavra "Bearer " do cabeçalho e pegar só o código
    private String recoverToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}