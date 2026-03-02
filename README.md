# 📚 API Livraria - Sistema de Gerenciamento de Livros e Autores

Uma API RESTful robusta desenvolvida em **Java 21** e **Spring Boot 4.0** para o gerenciamento de um catálogo de livros e autores.

Este projeto foi construído com foco em boas práticas de Engenharia de Software, arquitetura limpa, e padrões de mercado, servindo como uma base sólida para sistemas de nível de produção.

## 🚀 Tecnologias e Ferramentas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 4.0
* **Banco de Dados:** PostgreSQL (via Docker)
* **Migrações de Banco:** Flyway
* **ORM:** Spring Data JPA / Hibernate
* **Documentação:** Swagger / Springdoc OpenAPI 3
* **Testes:** JUnit 5 e Mockito

## 🧠 Conceitos Aplicados e Arquitetura

Este projeto vai muito além de um CRUD básico, aplicando conceitos avançados de desenvolvimento backend:

* **Arquitetura RESTful & SOLID:** Separação clara de responsabilidades entre `Controllers` (camada de requisição/resposta), `Services` (regras de negócio) e `Repositories` (acesso a dados), erradicando lógicas condicionais complexas das rotas.
* **Padrão DTO (Data Transfer Object):** Utilização de *Java Records* com construtores inteligentes (Reflection) para mapear entidades. Isso garante o encapsulamento dos dados sensíveis, formata as respostas JSON de forma limpa e previne erros clássicos como *Loop Infinito* (StackOverflowError) em relacionamentos bidirecionais.
* **Orientação a Objetos e Polimorfismo:** Implementação de Herança no banco de dados. O sistema diferencia livros do tipo `Fisico` e `Ebook`, aplicando regras de negócio específicas (como descontos) através de interfaces como `Promocional`, garantindo que livros físicos sejam barrados de promoções digitais de forma segura.
* **Relacionamentos de Banco de Dados (JPA):** Mapeamento estrito (`validate`) de relacionamentos `@ManyToOne` e `@OneToMany` entre Autores e Livros, garantindo integridade referencial.
* **Tratamento Global de Exceções:** Implementação de um `@ControllerAdvice` para interceptar erros em tempo de execução (como *EntityNotFoundException* e *IllegalArgumentException*) e traduzi-los para respostas HTTP amigáveis e padronizadas (ex: `404 Not Found` e `400 Bad Request`), mantendo o log do servidor limpo.
* **Infraestrutura como Código (Containers):** Banco de dados isolado rodando em contêineres **Docker**, simulando um ambiente de infraestrutura real.
* **Versionamento de Banco de Dados:** Uso do **Flyway** para garantir que as mudanças de schema (`V1__criar_tabelas.sql`) sejam reprodutíveis, rastreáveis e consistentes em qualquer ambiente.
* **Testes Automatizados:** Testes de Unidade focados nas regras de negócio da aplicação usando **JUnit** e simulação do acesso ao banco de dados com **Mockito**, garantindo a cobertura do "caminho feliz" e das exceções da API.
* **Documentação Interativa:** API totalmente documentada via **Swagger UI**, oferecendo um contrato claro e testável para clientes Front-end e Mobile.

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
Com o banco rodando, inicie a aplicação via Maven. O Flyway se encarregará de criar as tabelas automaticamente.
```
mvn spring-boot:run
```
Documentação da API (Swagger)
Com o servidor rodando, acesse a documentação interativa pelo navegador:
👉 http://localhost:8080/swagger-ui/index.html

Pelo Swagger, é possível visualizar o catálogo de rotas, os schemas esperados, e executar testes diretos (GET, POST, PUT, DELETE) contra o banco de dados.