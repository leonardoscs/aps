package biblioteca.repositorio;

import java.util.List;

import biblioteca.entidades.Categoria;

public interface RepositorioCategoria extends Repositorio<Categoria> {

  List<Categoria> buscarTodas();

  Categoria buscarPeloNome(String nome);
}
