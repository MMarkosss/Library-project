package MarkLivraria.api.infra.security.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration // Ensina o Spring a ler essa classe logo que o projeto ligar
@RequiredArgsConstructor // Lombok fazendo o trabalho pesado das injeções de depends.
@EnableWebSecurity // Avisa que vamos personalizar as regras de segurança
public class SecurityConfigurations {

    // 1. INJETE O SEU NOVO FILTRO AQUI
    private final SecurityFilter securityFilter;

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    // Configuração do Filtro e Controle de Acesso
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desliga a proteção contra ataques CSRF (pois usaremos Tokens JWT que já são seguros)
                .csrf(csrf -> csrf.disable())

                // 2. Avisa que a nossa API é REST (Stateless). Não guardamos "Sessão" na memória.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. O Manual de Regras de quem entra e quem é barrado
                .authorizeHttpRequests(req -> {
                    // Libera o Swagger para todo mundo (permitAll)
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll();

                    // Libera a vitrine da loja: Qualquer um pode dar GET nos livros e autores
                    req.requestMatchers(HttpMethod.GET, "/livros/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/autores/**").permitAll();
                    req.requestMatchers(HttpMethod.GET, "/tags/**").permitAll();

                    // Libera a futura rota de login
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll();

                    // Libera a rota de cadastro para Users
                    req.requestMatchers(HttpMethod.POST, "/usuarios/registrar").permitAll();

                    // Permite que o Spring Boot acesse a rota de montagem de erros
                    req.requestMatchers("/error").permitAll();

                    // --- NOVA REGRA DE AUTORIZAÇÃO AQUI ---
                    // 2. Se a requisição chegou aqui, sabemos que NÃO É GET (é Post, Put ou Delete).
                    // Então, trancamos a rota inteira apenas para quem é Chefe.
                    req.requestMatchers("/livros/**").hasRole("ADMIN");
                    req.requestMatchers("/autores/**").hasRole("ADMIN");
                    req.requestMatchers("/tags/**").hasRole("ADMIN");

                    // Qualquer outra requisição (POST, PUT, DELETE) PRECISA estar autenticada
                    req.anyRequest().authenticated();
                })
                // 2. ADICIONE ESTA LINHA AQUI! (Avisa para rodar o seu filtro antes do filtro padrão do Spring)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                // --- NOVA CONFIGURAÇÃO DE ERROS AQUI ---
                .exceptionHandling(ex -> ex
                        // 1. Erro 401: Quem não mandou token, ou mandou token expirado/falso
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"erro\": \"Acesso não autorizado. Faça login para obter um token válido.\"}");
                        })
                        // 2. Erro 403: Quem está logado (tem token), mas tentou fazer algo que não tem permissão
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"erro\": \"Você não tem permissão para acessar este recurso.\"}");
                        })
                )
                // ---------------------------------------

                .build();

    }
    // Ensina o Spring Security a injetar o Gerenciador de Autenticação na nossa Controller depois
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Ensina o Spring Security a usar o BCrypt para comparar as senhas digitadas com as do banco
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}