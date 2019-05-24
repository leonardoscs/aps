package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.Editora;

public interface RepositorioEditora extends Repositorio<Editora> {

  List<Editora> buscarTodos();

  // TODO: adicinar indice
  Editora buscarPeloNome(String nome);

}
