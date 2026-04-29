package MarkLivraria.api.infra.security.services;


import MarkLivraria.api.features.users.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // O Spring vai ler o application.properties e injetar o valor "12345678" aqui
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            // Escolhemos o algoritmo de criptografia HMAC256 e passamos o nosso segredo
            var algorithm = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("API Livraria") // Quem emitiu a pulseira?
                    .withSubject(user.getUsername()) // De quem é a pulseira?
                    .withExpiresAt(expirationDate()) // Quando ela perde a validade?
                    .sign(algorithm); // Assina e finaliza!

        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    // Método novo para validar a pulseira e extrair o e-mail do usuário
    public String getSubject(String jwtToken) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("API Livraria")
                    .build()
                    .verify(jwtToken) // O Auth0 verifica se a assinatura é válida e se não expirou
                    .getSubject();    // Se estiver tudo OK, devolve o e-mail (marcos@email.com)
        } catch (com.auth0.jwt.exceptions.JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    // Regra de negócio: O Token dura exatas 2 horas a partir do momento do login
    private Instant expirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}