package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.TipoUsuario;

public interface RepositorioTipoUsuario extends Repositorio<TipoUsuario> {

  List<TipoUsuario> buscarTodos();

  // TODO: criar indice para descricao?
  TipoUsuario buscarPelaDescricao(String descricao);
}
