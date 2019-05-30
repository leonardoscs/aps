import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import biblioteca.entidades.Autor;
<<<<<<< HEAD
=======
import biblioteca.entidades.TipoUsuario;
import biblioteca.entidades.Usuario;
>>>>>>> 582c9d30e5528d4d00f603a85f79fb025f5d02b8
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
<<<<<<< HEAD
=======

    /*
    // Exemplo como cadastrar um usuário.
    // Tipo já deve ter sido previamente cadastrado no banco...
    TipoUsuario tipo = repoTipo.buscarPelaDescricao("Aluno");

    Usuario usuario = new Usuario();
    usuario.setNome("Foo");
    usuario.setEmail("foo@email.com");
    usuario.setTelefone("322232323");
    usuario.setMatricula(32020002323L);
    usuario.setTipo(tipo);

    repoUsuario.cadastrar(usuario); // Cadastra usuário

    System.out.println("Usuario cadastrado: " + usuario);

    repoUsuario.deletarPeloId(usuario.getId()); // Deleta usuário
    */
>>>>>>> 582c9d30e5528d4d00f603a85f79fb025f5d02b8
  }
}
