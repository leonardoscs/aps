package biblioteca.repositorio.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import biblioteca.repositorio.*;

public class GerenciadorRepositorioSQL extends GerenciadorRepositorio {

  private String url;
  private String senha;
  private String usuario;

  public GerenciadorRepositorioSQL(String url, String usuario, String senha) {
    this.url = url;
    this.senha = senha;
    this.usuario = usuario;
  }

  @Override
  public void inicializar() throws Exception {
    Connection conn = DriverManager.getConnection(this.url, this.usuario, this.senha);
    
    RepositorioAutor repoAutor = new RepositorioAutorSQL(conn);
    RepositorioEditora repoEditora = new RepositorioEditoraSQL(conn);
    RepositorioLivro repoLivro = new RepositorioLivroSQL(conn);
    RepositorioUsuario repoUsuario = new RepositorioUsuarioSQL(conn);
    RepositorioTipoUsuario repoTipo = new RepositorioTipoUsuarioSQL(conn);
    RepositorioCategoria repoCat = new RepositorioCategoriaSQL(conn);
    RepositorioExemplarLivro repoExemplar = new RepositorioExemplarLivroSQL(conn, repoLivro);
    RepositorioEmprestimo repoEmp = new RepositorioEmprestimoSQL(conn, repoExemplar, repoUsuario);
    RepositorioRestricao repoRestricao = new RepositorioRestricaoSQL(conn);
    
    adicionarRepositorio(repoAutor);
    adicionarRepositorio(repoEditora);
    adicionarRepositorio(repoUsuario);
    adicionarRepositorio(repoLivro);
    adicionarRepositorio(repoTipo);
    adicionarRepositorio(repoCat);
    adicionarRepositorio(repoExemplar);
    adicionarRepositorio(repoEmp);
    adicionarRepositorio(repoRestricao);
  }

}
