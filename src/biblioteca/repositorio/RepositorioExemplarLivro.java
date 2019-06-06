package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Livro;

public interface RepositorioExemplarLivro extends Repositorio<ExemplarLivro> {

  List<ExemplarLivro> buscarExemplares(Livro livro);

}