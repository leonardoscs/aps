package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import biblioteca.entidades.TipoUsuario;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.RepositorioUsuario;

public class RepositorioUsuarioSQL implements RepositorioUsuario {
  
  private Connection conn;
  
  public RepositorioUsuarioSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public Usuario buscarPeloId(int id) {
    String sql = 
      "  SELECT" +
      "    id_usuario," +
      "    nome," +
      "    telefone," +
      "    email," +
      "    matricula," +
      "    tipo_usuario," +
      "    tu.descricao," +
      "    qtd_dias_emprestimo" +
      "  FROM" +
      "    usuario u" +
      "  JOIN" +
      "    tipo_usuario tu ON (tu.id_tipo = u.tipo_usuario)" +
      "  WHERE " +
      "    id_usuario = ?";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id_usuario"));
        usuario.setNome(rs.getString("nome"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setEmail(rs.getString("email"));
        usuario.setMatricula(rs.getLong("matricula"));
        
        TipoUsuario tipoUsuario = new TipoUsuario();
        tipoUsuario.setId(rs.getInt("tipo_usuario"));
        tipoUsuario.setDescricao(rs.getString("descricao"));
        tipoUsuario.setQuantidadeDiasEmprestimo(rs.getShort("qtd_dias_emprestimo"));
        
        usuario.setTipo(tipoUsuario);
        
        return usuario;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(Usuario usuario) {
    String sql = 
        "INSERT INTO usuario (nome, tipo_usuario, telefone, email, matricula) " + 
        "VALUES (?, ?, ?, ?, ?) RETURNING id_usuario";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, usuario.getNome());
      st.setInt(2, usuario.getTipo().getId());
      st.setString(3, usuario.getTelefone());
      st.setString(4, usuario.getEmail());
      st.setLong(5, usuario.getMatricula());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      usuario.setId(rs.getInt("id_usuario"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM usuario WHERE id_usuario = ?")) { //  RETURNING id_autor, nome;
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void atualizar(Usuario usuario) {
    String sql = 
        "UPDATE usuario SET tipo_usuario = ?, nome = ?, telefone = ?, email = ?, matricula = ? " +
        "WHERE id_usuario = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, usuario.getTipo().getId());
      st.setString(2, usuario.getNome());
      st.setString(3, usuario.getTelefone());
      st.setString(4, usuario.getEmail());
      st.setLong(5, usuario.getMatricula());
      st.setInt(6, usuario.getId());
      
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Usuario buscarPelaMatricula(long matricula) {
    // TODO: ctrl+c ctrl+v do buscarPeloId... criar um método comum entre eles
    String sql = 
        "  SELECT" +
        "    id_usuario," +
        "    nome," +
        "    telefone," +
        "    email," +
        "    matricula," +
        "    tipo_usuario," +
        "    tu.descricao," +
        "    qtd_dias_emprestimo" +
        "  FROM" +
        "    usuario u" +
        "  JOIN" +
        "    tipo_usuario tu ON (tu.id_tipo = u.tipo_usuario)" +
        "  WHERE " +
        "    matricula = ?";
      
      try (PreparedStatement st = conn.prepareStatement(sql)) {
        st.setLong(1, matricula);
        
        ResultSet rs = st.executeQuery();
        if (rs.next()) {
          Usuario usuario = new Usuario();
          usuario.setId(rs.getInt("id_usuario"));
          usuario.setNome(rs.getString("nome"));
          usuario.setTelefone(rs.getString("telefone"));
          usuario.setEmail(rs.getString("email"));
          usuario.setMatricula(rs.getLong("matricula"));
          
          TipoUsuario tipoUsuario = new TipoUsuario();
          tipoUsuario.setId(rs.getInt("tipo_usuario"));
          tipoUsuario.setDescricao(rs.getString("descricao"));
          tipoUsuario.setQuantidadeDiasEmprestimo(rs.getShort("qtd_dias_emprestimo"));
          
          usuario.setTipo(tipoUsuario);
          
          return usuario;
        }
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      return null;
  }
}
