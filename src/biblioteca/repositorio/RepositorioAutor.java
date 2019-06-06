package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.Autor;

public interface RepositorioAutor extends Repositorio<Autor> {

  // TODO: talvez adicionar paginação
  List<Autor> buscarTodos();

  // TODO: adicinar indice
  Autor buscarPeloNome(String nome);

  List<Autor> buscarPeloNomeParcial(String nome);
}
