package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.Autor;

public interface RepositorioAutor extends Repositorio<Autor> {

  List<Autor> buscarTodos();

  // TODO: talvez adicionar paginação
}
