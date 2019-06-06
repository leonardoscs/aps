package biblioteca.repositorio;
import biblioteca.entidades.Usuario;

public interface RepositorioUsuario extends Repositorio<Usuario> {

  Usuario buscarPelaMatricula(long matricula);

}