import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import biblioteca.entidades.Autor;
import biblioteca.repositorio.RepositorioAutor;
import biblioteca.repositorio.RepositorioCategoria;
import biblioteca.repositorio.RepositorioEditora;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioLivro;
import biblioteca.repositorio.RepositorioTipoUsuario;
import biblioteca.repositorio.RepositorioUsuario;
import biblioteca.repositorio.sql.RepositorioAutorSQL;
import biblioteca.repositorio.sql.RepositorioCategoriaSQL;
import biblioteca.repositorio.sql.RepositorioEditoraSQL;
import biblioteca.repositorio.sql.RepositorioEmprestimoSQL;
import biblioteca.repositorio.sql.RepositorioExemplarLivroSQL;
import biblioteca.repositorio.sql.RepositorioLivroSQL;
import biblioteca.repositorio.sql.RepositorioTipoUsuarioSQL;
import biblioteca.repositorio.sql.RepositorioUsuarioSQL;

public class Testes {

  public static void main(String[] args) throws SQLException, ClassNotFoundException {
    Class.forName("org.postgresql.Driver");
    
    Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/biblioteca", "aps", "123");
    
    RepositorioAutor repoAutor = new RepositorioAutorSQL(conn);
    RepositorioEditora repoEditora = new RepositorioEditoraSQL(conn);

    RepositorioLivro repoLivro = new RepositorioLivroSQL(conn);
    RepositorioUsuario repoUsuario = new RepositorioUsuarioSQL(conn);
    RepositorioTipoUsuario repoTipo = new RepositorioTipoUsuarioSQL(conn);
    RepositorioCategoria repoCat = new RepositorioCategoriaSQL(conn);
    RepositorioExemplarLivro repoExemplar = new RepositorioExemplarLivroSQL(conn, repoLivro);
    RepositorioEmprestimo repoEmp = new RepositorioEmprestimoSQL(conn, repoExemplar);

    /*List<Editora> editoras = repoEditora.buscarTodos();
    for (Editora editora : editoras) {
      System.out.printf("Id: %d, Nome: %s\n", editora.getId(),editora.getNome());
    }*/
    
    /*List<Autor> autores = repoAutor.buscarTodos();
    for (Autor autor : autores) {
      System.out.printf("Id: %d, Nome: %s\n", autor.getId(), autor.getNome());
    }*/
    
    /* Exemplo cadastrar autor
    Autor autor = new Autor();
    autor.setNome("George Orwell");
    
    repoAutor.cadastrar(autor);
    
    System.out.println("Autor cadastrado: " + autor);
    */
  }
}
