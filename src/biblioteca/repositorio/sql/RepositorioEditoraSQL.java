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

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM editora WHERE id_editora = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
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
  
}
