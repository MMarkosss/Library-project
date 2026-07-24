# 📚 API Livraria - Sistema Transacional de Gerenciamento de Livros

**Demonstração do Assistente Virtual (IA):**
<br>
<a href="https://ibb.co/Kzq3t44m"><img src="https://i.ibb.co/Kzq3t44m/Kooha-2026-07-22-13-22-50.gif" alt="Kooha-2026-07-22-13-22-50" border="0" /></a>

> ⚠️ **Status do Projeto: Arquivado / Concluído**
> *Este sistema foi construído como um laboratório de estudos para consolidar conhecimentos em arquitetura e desenvolvimento backend. Por se tratar de um cenário fictício que já cumpriu seu propósito educacional, o desenvolvimento ativo foi encerrado. No momento, meu foco está voltado integralmente para a resolução de problemas e projetos do mundo real.*

Uma API RESTful robusta desenvolvida em Java 21 e Spring Boot 4.0 para o gerenciamento de um catálogo de livros, controle de estoque, locações e vendas, protegida por autenticação JWT. Este projeto foi construído com foco em boas práticas de Engenharia de Software, arquitetura limpa e padrões de mercado, servindo como uma base sólida para sistemas de nível de produção. Adicionalmente, conta com um módulo de atendimento integrado via Inteligência Artificial.

## 🚀 Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.0
* **Segurança:** Spring Security & Auth0 JWT (JSON Web Token)
* **Banco de Dados:** PostgreSQL (via Docker)
* **Migrações de Banco:** Flyway
* **ORM:** Spring Data JPA / Hibernate
* **Produtividade:** Lombok (Boilerplate Zero)
* **Documentação:** Swagger / Springdoc OpenAPI 3.
* **Testes:** JUnit 5 e Mockito (BDDMockito).
* **Integração IA:** Google Gemini API e Spring `RestClient`.

## 🧠 Conceitos Aplicados e Arquitetura

Este projeto vai muito além de um CRUD básico, aplicando conceitos avançados de desenvolvimento backend e regras de negócio complexas[cite: 2]:

* **Assistente Virtual com IA Generativa:** Implementação de um chatbot integrado à API do Google Gemini (`gemini-flash-lite-latest`) para atendimento e sanar dúvidas gerais. A comunicação HTTP externa é feita de forma síncrona utilizando o `RestClient` nativo do Spring. A integração utiliza injeção de chaves via variáveis de ambiente e o envio seguro por cabeçalhos (`x-goog-api-key`), estruturando o *payload* do Gemini através do encapsulamento em *Java Records*.
* **Segurança e RBAC (Role-Based Access Control):** API trancada via Spring Security[cite: 2]. Implementação de autenticação *stateless* com tokens JWT e criptografia de senhas via BCrypt[cite: 2]. Controle de acesso estrito onde `ADMIN` gerencia o catálogo e `USER` realiza transações (compras e empréstimos)[cite: 2]. Rotas públicas específicas para registro de novos clientes[cite: 2].
* **Arquitetura RESTful & SOLID:** Separação clara de responsabilidades entre `Controllers`, `Services` e `Repositories`[cite: 2]. Utilização de injeção de dependência limpa via construtores (com `@RequiredArgsConstructor` do Lombok), abolindo o uso do `@Autowired` em propriedades[cite: 2].
* **Transações e Integridade de Dados (`@Transactional`):** Módulo de vendas e empréstimos com validação de regras de negócio estritas[cite: 2]. O sistema gerencia o estoque de livros físicos automaticamente, efetuando *rollback* em caso de falhas para garantir a consistência do banco de dados[cite: 2].
* **Polimorfismo e Herança no Banco (JOINED):** O sistema diferencia nativamente `Ebook` (com controle de MB e Marca d'água) e `Fisico` (com controle de peso e estoque)[cite: 2]. Utilização de Factory Pattern e Pattern Matching (`instanceof`) do Java 21 para instanciar e atualizar (PATCH) as classes filhas de forma totalmente segura[cite: 2].
* **Relacionamentos Complexos (JPA):** `N:N` (Muitos-para-Muitos) entre Livros e Tags utilizando `@JoinTable`[cite: 2]. `1:N` / `N:1` (Um-para-Muitos) entre Autores, Livros, Usuários, Compras e Empréstimos, com otimização rigorosa de consultas usando `FetchType.LAZY`[cite: 2].
* **Padrão DTO (Data Transfer Object):** Utilização de *Java Records* para o tráfego de dados[cite: 2]. Garante encapsulamento, previne ataques de Mass Assignment e evita erros de Loop Infinito (StackOverflowError) na serialização JSON[cite: 2].
* **Tratamento Global de Exceções:** Uso de `@RestControllerAdvice` e tradutores do Spring Security (`AuthenticationEntryPoint` e `AccessDeniedHandler`) para interceptar exceções (ex: EntityNotFoundException, Tokens Expirados) e padronizar todas as respostas de erro da API em formato JSON amigável e legível para o Front-end[cite: 2].
* **Infraestrutura como Código:** Banco de dados isolado rodando em contêineres Docker[cite: 2]. Versionamento de schema gerido de forma automatizada pelo Flyway (desde a V1 de tabelas até as migrações de segurança e transações financeiras)[cite: 2].
* **Testes Automatizados de Unidade:** Testes focados no comportamento (BDD) das regras de negócio usando JUnit e simulação de repositórios com Mockito, garantindo a confiabilidade de cálculos e fábricas[cite: 2].

## ⚙️ Como Executar o Projeto Localmente

### Pré-requisitos
* Java 21[cite: 2]
* Maven[cite: 2]
* Docker e Docker Compose
* Chave de API do Google Gemini (Gerada gratuitamente no Google AI Studio)


### Passo 1: Configurar a Chave da API
Para habilitar o módulo do Assistente Virtual, você precisa configurar a chave de acesso da inteligência artificial. No seu terminal, defina a seguinte variável de ambiente:
```bash
export GEMINI_API_KEY="cole_sua_chave_gerada_aqui"
```

Passo 2: Subir o Banco de Dados (Docker)

Abra o seu terminal na raiz do projeto e execute o comando abaixo para iniciar o contêiner do PostgreSQL utilizando o arquivo de configuração existente:

```bash
docker compose up -d db
```

Passo 3: Executar a Aplicação Spring Boot

Com o banco rodando e a variável da API injetada, inicie a aplicação. O Flyway se encarregará de criar todas as tabelas, relacionamentos e inserir o usuário ADMIN padrão automaticamente.

```bash
./mvnw clean spring-boot:run
```

Passo 4: Testando a API

A aplicação suporta testes integrados através de arquivos nativos .http do IntelliJ IDEA (presentes na raiz do projeto), que automatizam o fluxo de Login, captura de Token JWT, requisições autenticadas e a chamada pública para o /chatbot.

Você também pode acessar a documentação interativa (Swagger) pelo navegador para ler os contratos e testar as rotas:
👉 http://localhost:8080/swagger-ui/index.html
