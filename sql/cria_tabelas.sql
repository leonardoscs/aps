BEGIN TRANSACTION;

CREATE TABLE livro (
  id_livro          SERIAL NOT NULL,
  titulo            VARCHAR(120) NOT NULL,
  descricao         TEXT NOT NULL,
  qtd_paginas       SMALLINT NOT NULL,
  data_publicacao   DATE NOT NULL,
  localizacao       VARCHAR(30) NOT NULL,
  id_editora        INT NOT NULL  -- fk
);

CREATE TABLE editora (
  id_editora    SERIAL NOT NULL,
  nome          VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE autor (
  id_autor      SERIAL NOT NULL,
  nome          VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE exemplar_livro (
  id_exemplar   SERIAL NOT NULL,
  id_livro      INT NOT NULL,    -- fk
  disponivel    BOOLEAN NOT NULL
);

CREATE TABLE livro_autor (
  id_livro  INT NOT NULL, -- fk
  id_autor  INT NOT NULL  -- fk
);

CREATE TABLE usuario (
  id_usuario    SERIAL NOT NULL,
  tipo_usuario  INT NOT NULL, -- fk
  nome          VARCHAR(70) NOT NULl,
  telefone      VARCHAR(15) NOT NUll,
  email         VARCHAR(100) NOT NULL UNIQUE, -- talvez possa ser nulo... ou talvez nem precise desse campo... sla
  matricula     BIGINT NOT NULL UNIQUE
);

CREATE TABLE tipo_usuario (
  id_tipo              SERIAL NOT NULL,
  descricao            VARCHAR(50) NOT NULL UNIQUE,
  qtd_dias_emprestimo  SMALLINT NOT NULL
);

CREATE TABLE emprestimo (
  id_emprestimo          SERIAL NOT NULL,
  id_usuario             INT NOT NULL, -- fk
  id_exemplar            INT NOT NULL, -- fk
  data_emprestou         DATE NOT NULL,
  data_devolveu          DATE NOT NULL,
  data_limite_devolucao  DATE NOT NULL
);

CREATE TABLE restricao (
  id_restricao SERIAL NOT NULL,
  id_usuario   INT NOT NULL,
  data_inicio  DATE NOT NULL,
  data_fim     DATE NOT NULL,
  motivo       VARCHAR(200) NOT NULL
);

CREATE TABLE categoria (
  id_categoria  SERIAL NOT NULL,
  nome          VARCHAR(70) NOT NULL
);

CREATE TABLE livro_categoria (
  id_categoria INT NOT NULL,
  id_livro     INT NOT NULL
);

ALTER TABLE livro ADD CONSTRAINT pk_id_livro PRIMARY KEY (id_livro);
ALTER TABLE editora ADD CONSTRAINT pk_id_editora PRIMARY KEY (id_editora);
ALTER TABLE autor ADD CONSTRAINT pk_id_autor PRIMARY KEY (id_autor);
ALTER TABLE exemplar_livro ADD CONSTRAINT pk_id_exemplar PRIMARY KEY (id_exemplar);
ALTER TABLE livro_autor ADD CONSTRAINT pk_id_livro_id_autor PRIMARY KEY (id_livro, id_autor);
ALTER TABLE usuario ADD CONSTRAINT pk_id_usuario PRIMARY KEY (id_usuario);
ALTER TABLE tipo_usuario ADD CONSTRAINT pk_id_tipo PRIMARY KEY (id_tipo);
ALTER TABLE emprestimo ADD CONSTRAINT pk_id_emprestimo PRIMARY KEY (id_emprestimo);
ALTER TABLE restricao ADD CONSTRAINT pk_id_restricao PRIMARY KEY (id_restricao);
ALTER TABLE categoria ADD CONSTRAINT pk_id_categoria PRIMARY KEY (id_categoria);
ALTER TABLE livro_categoria ADD CONSTRAINT pk_id_categoria_id_livro PRIMARY KEY (id_categoria, id_livro);

ALTER TABLE livro 
  ADD CONSTRAINT fk_livro_id_editora FOREIGN KEY (id_editora) REFERENCES editora (id_editora);
  
ALTER TABLE exemplar_livro 
  ADD CONSTRAINT fk_exemplar_livro_id_livro FOREIGN KEY (id_livro) REFERENCES livro (id_livro);

ALTER TABLE livro_autor 
  ADD CONSTRAINT fk_livro_autor_id_livro FOREIGN KEY (id_livro) REFERENCES livro (id_livro),
  ADD CONSTRAINT fk_livro_autor_id_autor FOREIGN KEY (id_autor) REFERENCES autor (id_autor);
  
ALTER TABLE usuario 
  ADD CONSTRAINT fk_usuario_tipo_usuario FOREIGN KEY (tipo_usuario) REFERENCES tipo_usuario (id_tipo);

ALTER TABLE emprestimo 
  ADD CONSTRAINT fk_emprestimo_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario),
  ADD CONSTRAINT fk_emprestimo_id_exemplar FOREIGN KEY (id_exemplar) REFERENCES exemplar_livro (id_exemplar);

ALTER TABLE restricao
  ADD CONSTRAINT fk_restricao_id_usuario FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario);
  
ALTER TABLE livro_categoria
  ADD CONSTRAINT fk_livro_categoria_id_livro FOREIGN KEY (id_livro) REFERENCES livro (id_livro),
  ADD CONSTRAINT fk_livro_categoria_id_categoria FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria);

--ROLLBACK;
COMMIT;