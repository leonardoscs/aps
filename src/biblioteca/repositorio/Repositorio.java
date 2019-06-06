package biblioteca.repositorio;

public interface Repositorio<T> {
  T buscarPeloId(int id);
  
  void cadastrar(T entidade);
  
  void atualizar(T entidade);

  void deletarPeloId(int i);
}
