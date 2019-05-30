package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.TipoUsuario;

public interface RepositorioTipoUsuario extends Repositorio<TipoUsuario> {

  List<TipoUsuario> buscarTodos();

<<<<<<< HEAD
=======
  // TODO: criar indice para descricao?
  TipoUsuario buscarPelaDescricao(String descricao);
>>>>>>> 582c9d30e5528d4d00f603a85f79fb025f5d02b8
}
