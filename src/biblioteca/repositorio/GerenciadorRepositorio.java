package biblioteca.repositorio;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class GerenciadorRepositorio {

  private Map<Class<?>, Repositorio<?>> repositorios = new HashMap<>();

  public abstract void inicializar() throws Exception;
  
  @SuppressWarnings("unchecked")
  public <T extends Repositorio<?>> T getRepositorio(Class<T> classe) {
    Repositorio<?> repo = repositorios.get(classe);
    if (repo == null) {
      throw new RuntimeException(String.format("O repositorio '%s' não foi cadastrado.", 
          classe.getSimpleName()));
    }
    return (T) repo;
  }
  
  public void adicionarRepositorio(Repositorio<?> repo) {
    // Cadastra pela classe da interface, não a da implementação.
    // Por exemplo, RepositorioUsuario ao invés de RepositorioUsuarioSQL.
    Class<?> classeRepositorio = Arrays.stream(repo.getClass().getInterfaces())
        .filter(Repositorio.class::isAssignableFrom)
        .findFirst().get();
    
    repositorios.put(classeRepositorio, repo);
  }

}
