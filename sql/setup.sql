\! echo Deletando usuario e databases antigos (caso existam)...

-- Deleta usuario aps e databases
DROP DATABASE IF EXISTS biblioteca;
DROP DATABASE IF EXISTS biblioteca_testes;

DROP USER IF EXISTS aps;

\! echo Criando usuario aps (senha: 123)...
-- Cria usuario aps e databases
CREATE USER aps WITH password '123';
ALTER ROLE aps CREATEDB;

-- Conecta ao template com o usuario aps para
-- poder criar as databases
\c template1 aps

\! echo Criando database 'biblioteca'...
CREATE DATABASE biblioteca;

\! echo Criando database 'biblioteca_testes'...
CREATE DATABASE biblioteca_testes;

-- Conecta a database biblioteca
\c biblioteca

\! echo Criando tabelas...

\i cria_tabelas.sql

\! echo Inserindo dados...

\i dados.sql

\i dados_livros.sql

\! pause