package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.TipoUsuario;
import biblioteca.repositorio.RepositorioTipoUsuario;

public class RepositorioTipoUsuarioSQL implements RepositorioTipoUsuario {
  
  private Connection conn;
  
  public RepositorioTipoUsuarioSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public TipoUsuario buscarPeloId(int id) {
    String sql = 
      "SELECT id_tipo, descricao, qtd_dias_emprestimo FROM tipo_usuario WHERE id_tipo = ?";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(rs.getInt("id_tipo"));
        tipo.setDescricao(rs.getString("descricao"));
        tipo.setQuantidadeDiasEmprestimo(rs.getShort("qtd_dias_emprestimo"));
        return tipo;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(TipoUsuario tipoUsuario) {
    String sql = 
        "INSERT INTO tipo_usuario (descricao, qtd_dias_emprestimo) VALUES (?, ?) RETURNING id_tipo";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, tipoUsuario.getDescricao());
      st.setShort(2, (short) tipoUsuario.getQuantidadeDiasEmprestimo());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      tipoUsuario.setId(rs.getInt("id_tipo"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM tipo_usuario WHERE id_tipo = ?")) { //  RETURNING id_autor, nome;
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<TipoUsuario> buscarTodos() {
    List<TipoUsuario> todos = new ArrayList<>();
    try (PreparedStatement st = conn.prepareStatement("SELECT id_tipo, descricao, qtd_dias_emprestimo FROM tipo_usuario")) {
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(rs.getInt("id_tipo"));
        tipo.setDescricao(rs.getString("descricao"));
        tipo.setQuantidadeDiasEmprestimo(rs.getShort("qtd_dias_emprestimo"));
        todos.add(tipo);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return todos;
  }
<<<<<<< HEAD
  
=======

  @Override
  public TipoUsuario buscarPelaDescricao(String descricao) {
    String sql =
      "SELECT id_tipo, descricao, qtd_dias_emprestimo FROM tipo_usuario WHERE lower(descricao) = lower(?)";

    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, descricao);

      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        TipoUsuario tipo = new TipoUsuario();
        tipo.setId(rs.getInt("id_tipo"));
        tipo.setDescricao(rs.getString("descricao"));
        tipo.setQuantidadeDiasEmprestimo(rs.getShort("qtd_dias_emprestimo"));
        return tipo;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

>>>>>>> 582c9d30e5528d4d00f603a85f79fb025f5d02b8
  @Override
  public void atualizar(TipoUsuario tipo) {
    String sql = 
        "UPDATE tipo_usuario SET descricao = ?, qtd_dias_emprestimo = ? WHERE id_tipo = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, tipo.getDescricao());
      st.setShort(2, (short) tipo.getQuantidadeDiasEmprestimo());
      st.setInt(3, tipo.getId());
      
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
