select 
  titulo, 
  editora.nome as nome_editora,
  qtd_emprestimos,
  (
    select string_agg(autor.nome, ', ') as autores 
    from livro_autor la 
    join autor on (la.id_autor = autor.id_autor) 
    where la.id_livro = livro.id_livro
  ) as autores
from livro 
join (
  select 
    id_livro, count(*) as qtd_emprestimos 
  from 
    emprestimo e 
  join 
    exemplar_livro el on (el.id_exemplar = e.id_exemplar) 
  where 
    date_part('year', data_emprestou) = date_part('year', CURRENT_DATE) - 1
  group by 
    id_livro
) tb on (livro.id_livro = tb.id_livro) 
join 
  editora on (livro.id_editora = editora.id_editora)
order by qtd_emprestimos desc
limit 20;