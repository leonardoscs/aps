package biblioteca.repositorio;

// TODO: O método buscarTodos é comum a várias entidades, talvez ele devesse ficar aqui...
public interface Repositorio<T> {
  T buscarPeloId(int id);
  
  void cadastrar(T entidade);
  
  void atualizar(T entidade);

  void deletarPeloId(int i);
}
