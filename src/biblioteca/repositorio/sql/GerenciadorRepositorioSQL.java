package biblioteca.repositorio.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioAutor;
import biblioteca.repositorio.RepositorioCategoria;
import biblioteca.repositorio.RepositorioEditora;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioLivro;
import biblioteca.repositorio.RepositorioTipoUsuario;
import biblioteca.repositorio.RepositorioUsuario;

public class GerenciadorRepositorioSQL extends GerenciadorRepositorio {

  @Override
  public void inicializar() throws Exception {
    // TODO: receber a conexão pelo constructor? sla
    // TODO: adicionar opção para especificar url/senha/usuario do banco?
    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aps", "123");
    
    RepositorioAutor repoAutor = new RepositorioAutorSQL(conn);
    RepositorioEditora repoEditora = new RepositorioEditoraSQL(conn);
    RepositorioLivro repoLivro = new RepositorioLivroSQL(conn);
    RepositorioUsuario repoUsuario = new RepositorioUsuarioSQL(conn);
    RepositorioTipoUsuario repoTipo = new RepositorioTipoUsuarioSQL(conn);
    RepositorioCategoria repoCat = new RepositorioCategoriaSQL(conn);
    RepositorioExemplarLivro repoExemplar = new RepositorioExemplarLivroSQL(conn, repoLivro);
    RepositorioEmprestimo repoEmp = new RepositorioEmprestimoSQL(conn, repoExemplar, repoUsuario);
    
    adicionarRepositorio(repoAutor);
    adicionarRepositorio(repoEditora);
    adicionarRepositorio(repoUsuario);
    adicionarRepositorio(repoLivro);
    adicionarRepositorio(repoTipo);
    adicionarRepositorio(repoCat);
    adicionarRepositorio(repoExemplar);
    adicionarRepositorio(repoEmp);
  }

}
