# 📚 API Livraria - Spring Boot RESTful

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-4479A1?style=for-the-badge&logo=databricks&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

Uma API RESTful completa para gerenciamento de um catálogo de livros e e-books. Este projeto foi desenvolvido como parte dos meus estudos em Engenharia de Computação (Univesp) com foco em aprofundar os conhecimentos no ecossistema Spring e boas práticas de arquitetura Backend.

## 🚀 Funcionalidades e Conceitos Aplicados

Este projeto vai além de um CRUD básico, implementando padrões reais do mercado corporativo:

* **Arquitetura em Camadas:** Separação clara entre `Controller`, `Service` e `Repository`.
* **Herança no Banco de Dados (JPA):** Utilização da estratégia `SINGLE_TABLE` para mapear polimorfismo entre as classes `Livro` e `Ebook` na mesma tabela do banco relacional, incluindo resolução de instâncias no JSON via `@JsonTypeInfo`.
* **Ocultação de Dados (DTO Pattern):** Uso de `Records` do Java moderno para transitar apenas os dados necessários para o cliente, evitando *Over-fetching*.
* **Validações de Entrada (Bean Validation):** Proteção das rotas contra dados inconsistentes utilizando `@Valid`, `@NotBlank`, `@Positive`, etc.
* **Tratamento Global de Exceções:** Implementação de `@RestControllerAdvice` para interceptar erros (como o Erro 400) e devolver mensagens em formato JSON limpo e padronizado.
* **Paginação e Ordenação:** Uso da interface `Pageable` do Spring Data JPA para entregar dados em lotes otimizados (ex: `?page=0&size=10&sort=preco,asc`).
* **Documentação Viva:** Interface gráfica do Swagger UI configurada para testes diretos no navegador.

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3** (Web, Data JPA, Validation)
* **Banco de Dados H2** (Em memória)
* **Springdoc OpenAPI** (Swagger UI)
* **Maven** (Gerenciador de Dependências)

## ⚙️ Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone [https://github.com/MMarkosss/Library-project.git](https://github.com/MMarkosss/Library-project.git)