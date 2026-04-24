# 📚 API Livraria - Sistema Transacional de Gerenciamento de Livros

Uma API RESTful robusta desenvolvida em **Java 21** e **Spring Boot 4.0** para o gerenciamento de um catálogo de livros, controle de estoque, locações e vendas, protegida por autenticação JWT.

Este projeto foi construído com foco em boas práticas de Engenharia de Software, arquitetura limpa, e padrões de mercado, servindo como uma base sólida para sistemas de nível de produção.

## 🚀 Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.0
* **Segurança:** Spring Security & Auth0 JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL (via Docker)
* **Migrações de Banco:** Flyway
* **ORM:** Spring Data JPA / Hibernate
* **Produtividade:** Lombok (Boilerplate Zero)
* **Documentação:** Swagger / Springdoc OpenAPI 3
* **Testes:** JUnit 5 e Mockito (BDDMockito)

## 🧠 Conceitos Aplicados e Arquitetura

Este projeto vai muito além de um CRUD básico, aplicando conceitos avançados de desenvolvimento backend e regras de negócio complexas:

* **Segurança e RBAC (Role-Based Access Control):** API trancada via Spring Security. Implementação de autenticação *stateless* com tokens JWT e criptografia de senhas via **BCrypt**. Controle de acesso estrito onde `ADMIN` gerencia o catálogo e `USER` realiza transações (compras e empréstimos). Rotas públicas específicas para registro de novos clientes.
* **Arquitetura RESTful & SOLID:** Separação clara de responsabilidades entre `Controllers`, `Services` e `Repositories`. Utilização de injeção de dependência limpa via construtores (com `@RequiredArgsConstructor` do Lombok), abolindo o uso do `@Autowired` em propriedades.
* **Transações e Integridade de Dados (`@Transactional`):** Módulo de vendas e empréstimos com validação de regras de negócio estritas. O sistema gerencia o estoque de livros físicos automaticamente, efetuando *rollback* em caso de falhas para garantir a consistência do banco de dados.
* **Polimorfismo e Herança no Banco (JOINED):** O sistema diferencia nativamente `Ebook` (com controle de MB e Marca d'água) e `Fisico` (com controle de peso e estoque). Utilização de **Factory Pattern** e **Pattern Matching (`instanceof`)** do Java 21 para instanciar e atualizar (PATCH) as classes filhas de forma totalmente segura.
* **Relacionamentos Complexos (JPA):** * `N:N` (Muitos-para-Muitos) entre Livros e Tags utilizando `@JoinTable`.
    * `1:N` / `N:1` (Um-para-Muitos) entre Autores, Livros, Usuários, Compras e Empréstimos, com otimização rigorosa de consultas usando `FetchType.LAZY`.
* **Padrão DTO (Data Transfer Object):** Utilização de *Java Records* para o tráfego de dados. Garante encapsulamento, previne ataques de *Mass Assignment* e evita erros de *Loop Infinito* (StackOverflowError) na serialização JSON.
* **Tratamento Global de Exceções:** Uso de `@RestControllerAdvice` e tradutores do Spring Security (`AuthenticationEntryPoint` e `AccessDeniedHandler`) para interceptar exceções (ex: *EntityNotFoundException*, Tokens Expirados) e padronizar todas as respostas de erro da API em formato JSON amigável e legível para o Front-end.
* **Infraestrutura como Código:** Banco de dados isolado rodando em contêineres **Docker**. Versionamento de schema gerido de forma automatizada pelo **Flyway** (desde a `V1` de tabelas até as migrações de segurança e transações financeiras).
* **Testes Automatizados de Unidade:** Testes focados no comportamento (BDD) das regras de negócio usando **JUnit** e simulação de repositórios com **Mockito**, garantindo a confiabilidade de cálculos e fábricas.

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
* [Java 21](https://jdk.java.net/21/)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)

### Passo 1: Subir o Banco de Dados (Docker)
Abra o seu terminal e execute o comando abaixo para iniciar o contêiner do PostgreSQL:
```bash
docker run --name postgres-livraria \
  -e POSTGRES_USER=marcos \
  -e POSTGRES_PASSWORD=12345 \
  -e POSTGRES_DB=livrariadb \
  -p 5432:5432 \
  -d postgres:15
```

Passo 2: Executar a Aplicação Spring Boot
Com o banco rodando, inicie a aplicação via Maven. O Flyway se encarregará de criar todas as tabelas, relacionamentos e inserir o usuário ADMIN padrão automaticamente.

```bash
mvn spring-boot:run
```

Passo 3: Testando a API
A aplicação suporta testes integrados através de arquivos nativos .http do IntelliJ IDEA (presentes na raiz do projeto), que automatizam o fluxo de Login, captura de Token JWT e requisições autenticadas.

Você também pode acessar a documentação interativa (Swagger) pelo navegador para ler os contratos e testar as rotas:
👉 http://localhost:8080/swagger-ui/index.html