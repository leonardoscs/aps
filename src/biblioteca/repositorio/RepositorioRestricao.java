package biblioteca.repositorio;
import biblioteca.entidades.Restricao;
import biblioteca.entidades.Usuario;

public interface RepositorioRestricao extends Repositorio<Restricao> {

  Restricao buscarRestricaoAtiva(Usuario usuario);

}
