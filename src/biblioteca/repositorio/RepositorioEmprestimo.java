package biblioteca.repositorio;
import java.util.List;

import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.Usuario;

public interface RepositorioEmprestimo extends Repositorio<Emprestimo> {

  void marcarEmprestimoComoDevolvido(Emprestimo emprestimo);
  
  List<Emprestimo> buscarEmprestimosAtivos(Usuario usuario);
  
}