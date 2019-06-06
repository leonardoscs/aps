package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.Editora;

public interface RepositorioEditora extends Repositorio<Editora> {

  List<Editora> buscarTodos();

  Editora buscarPeloNome(String nome);

}
