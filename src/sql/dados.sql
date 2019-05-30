BEGIN TRANSACTION;

INSERT INTO tipo_usuario (descricao, qtd_dias_emprestimo) VALUES ('Aluno', 7), ('Professor', 30);

INSERT INTO usuario (nome, tipo_usuario, telefone, email, matricula) values 
  ('Leonardo', 1, '74628372', 'leonardo@email.com', 32010001234),
  ('Joao', 1, '82738283', 'joao@email.com', 32010003232),
  ('Jorge', 2, '98072343', 'jorge@email.com', 32010005555);

COMMIT;