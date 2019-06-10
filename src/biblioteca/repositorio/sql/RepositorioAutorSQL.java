package biblioteca.repositorio.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.Autor;
import biblioteca.repositorio.RepositorioAutor;

public class RepositorioAutorSQL implements RepositorioAutor {
  
  private Connection conn;
  
  public RepositorioAutorSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public Autor buscarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_autor, nome FROM autor WHERE id_autor = ?")) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      
      if (rs.next()) {
        String nome = rs.getString("nome");
        return new Autor(id, nome);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(Autor autor) {
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO autor (nome) VALUES (?) RETURNING id_autor")) {  
      st.setString(1, autor.getNome());
      ResultSet rs = st.executeQuery();
      rs.next();
      // Retorna o id do autor
      autor.setId(rs.getInt("id_autor"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_autor WHERE id_autor = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM autor WHERE id_autor = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void atualizar(Autor autor) {
    try (PreparedStatement st = conn.prepareStatement("UPDATE autor SET nome ? WHERE id_autor = ?")) {  
      st.setString(1, autor.getNome());
      st.setInt(2, autor.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Autor> buscarTodos() {
    List<Autor> autores = new ArrayList<>();
    try (PreparedStatement st = conn.prepareStatement("SELECT id_autor, nome FROM autor")) {
      ResultSet rs = st.executeQuery();
      
      while (rs.next()) {
        String nome = rs.getString("nome");
        int id = rs.getInt("id_autor");
        autores.add(new Autor(id, nome));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return autores;
  }

  @Override
  public Autor buscarPeloNome(String nome) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_autor, nome FROM autor WHERE lower(nome) = lower(?)")) {
      st.setString(1, nome);

      ResultSet rs = st.executeQuery();

      if (rs.next()) {
        return new Autor(rs.getInt("id_autor"), rs.getString("nome"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public List<Autor> buscarPeloNomeParcial(String nome) {
    String sql = "SELECT id_autor, nome FROM autor WHERE lower(nome) LIKE lower('%' || ? || '%')";
    List<Autor> autores = new ArrayList<>();

    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, nome);

      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        autores.add(new Autor(rs.getInt("id_autor"), rs.getString("nome")));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return autores;
  }

}
