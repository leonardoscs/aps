select 
  usuario.nome as nome_usuario,
  case when qtd_emprestimos is null
    then 0
    else qtd_emprestimos
  end,
  coalesce(
    (select 
        date_part('year', data_emprestou) as ano
      from 
        emprestimo e 
      where 
        e.id_usuario = usuario.id_usuario
      group by ano
      order by ano desc limit 1
    ), 0) as ano_mais_ativo
from usuario
full outer join (
  select 
    id_usuario, count(*) as qtd_emprestimos 
  from 
    emprestimo e 
  group by 
    id_usuario
) tb on (usuario.id_usuario = tb.id_usuario) 
order by qtd_emprestimos desc;