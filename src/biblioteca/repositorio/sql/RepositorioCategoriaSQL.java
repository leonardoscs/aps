package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.Categoria;
import biblioteca.repositorio.RepositorioCategoria;

public class RepositorioCategoriaSQL implements RepositorioCategoria {
  
  private Connection conn;
  
  public RepositorioCategoriaSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public Categoria buscarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_categoria, nome FROM categoria WHERE id_categoria = ?")) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      
      if (rs.next()) {
        String nome = rs.getString("nome");
        return new Categoria(id, nome);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(Categoria categoria) {
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO categoria (nome) VALUES (?) RETURNING id_categoria")) {
      st.setString(1, categoria.getNome());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      categoria.setId(rs.getInt("id_categoria"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_categoria WHERE id_categoria = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (PreparedStatement st = conn.prepareStatement("DELETE FROM categoria WHERE id_categoria = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
 
  @Override
  public void atualizar(Categoria cat) {
    try (PreparedStatement st = conn.prepareStatement("UPDATE categoria SET nome ? WHERE id_categoria = ?")) {  
      st.setString(1, cat.getNome());
      st.setInt(2, cat.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Categoria> buscarTodas() {
    List<Categoria> categorias = new ArrayList<>();
    try (PreparedStatement st = conn.prepareStatement("SELECT id_categoria, nome FROM categoria")) {
      ResultSet rs = st.executeQuery();
      
      while(rs.next()) {
        int id = rs.getInt("id_categoria");
        String nome = rs.getString("nome");
        categorias.add(new Categoria(id, nome));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return categorias;
  }

  @Override
  public Categoria buscarPeloNome(String nome) {
    try (PreparedStatement st = conn.prepareStatement("SELECT id_categoria, nome FROM categoria WHERE lower(nome) = lower(?)")) {
      st.setString(1, nome);

      ResultSet rs = st.executeQuery();

      if (rs.next()) {
        return new Categoria(rs.getInt("id_categoria"), rs.getString("nome"));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
}
