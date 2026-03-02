CREATE TABLE autor (
                       id BIGSERIAL PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL,
                       biografia TEXT
);

CREATE TABLE tb_livros (
                           id BIGSERIAL PRIMARY KEY,
                           tipo_livro VARCHAR(31) NOT NULL,
                           titulo VARCHAR(255) NOT NULL,
                           preco DOUBLE PRECISION NOT NULL,
                           paginas INTEGER NOT NULL,
                           genero VARCHAR(100) NOT NULL,
                           data_lancamento DATE,
                           marca_dagua VARCHAR(255),
                           autor_id BIGINT NOT NULL,

                           CONSTRAINT fk_autor FOREIGN KEY (autor_id) REFERENCES autor (id)
);