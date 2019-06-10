package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.Editora;
import biblioteca.repositorio.RepositorioEditora;

public class RepositorioEditoraSQL implements RepositorioEditora {
  
  private Connection conn;
  
  public RepositorioEditoraSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public Editora buscarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_editora, nome FROM editora WHERE id_editora = ?")) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      
      if (rs.next()) {
        String nome = rs.getString("nome");
        return new Editora(id, nome);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(Editora editora) {
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO editora (nome) VALUES (?) RETURNING id_editora")) {
      st.setString(1, editora.getNome());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      editora.setId(rs.getInt("id_editora"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  private void deletar(int id) throws SQLException {
    String livrosDaEditoraSQL = "SELECT id_livro FROM livro WHERE id_editora = ?";

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_autor WHERE id_livro IN ("+livrosDaEditoraSQL+")")) {
      st.setInt(1, id);
      st.executeUpdate();
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_categoria WHERE id_livro IN ("+livrosDaEditoraSQL+")")) {
      st.setInt(1, id);
      st.executeUpdate();
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM emprestimo " +
      "WHERE id_exemplar IN (SELECT id_exemplar FROM exemplar_livro WHERE id_livro IN ("+livrosDaEditoraSQL+"))")) {
      st.setInt(1, id);
      st.executeUpdate();
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM exemplar_livro WHERE id_livro IN ("+livrosDaEditoraSQL+")")) {
      st.setInt(1, id);
      st.executeUpdate();
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro WHERE id_livro IN ("+livrosDaEditoraSQL+")")) {
      st.setInt(1, id);
      st.executeUpdate();
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM editora WHERE id_editora = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    boolean autoCommit;
    try {
      autoCommit = conn.getAutoCommit();
    } catch (SQLException e) {
      autoCommit = true; // True por padrão
    }

    // Desativa o auto commit
    try {
      conn.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      deletar(id);
    } catch (SQLException ex) {
      try {
        conn.rollback();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      throw new RuntimeException(ex);
    }

    // Restaura o auto commit
    try {
      conn.setAutoCommit(autoCommit);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public void atualizar(Editora editora) {
    try (PreparedStatement st = conn.prepareStatement("UPDATE editora SET nome ? WHERE id_editora = ?")) {  
      st.setString(1, editora.getNome());
      st.setInt(2, editora.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Editora> buscarTodos() {
    List<Editora> editoras = new ArrayList<>();
    try (PreparedStatement st = conn.prepareStatement("SELECT id_editora, nome FROM editora")) {
      ResultSet rs = st.executeQuery();
      
      while (rs.next()) {
        String nome = rs.getString("nome");
        int id = rs.getInt("id_editora");
        editoras.add(new Editora(id, nome));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return editoras;
  }

  @Override
  public Editora buscarPeloNome(String nome) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_editora, nome FROM editora WHERE lower(nome) = lower(?)")) {
      st.setString(1, nome);

      ResultSet rs = st.executeQuery();

      if (rs.next()) {
        return new Editora(rs.getInt("id_editora"), rs.getString("nome"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
  
}
