package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import biblioteca.entidades.Restricao;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.RepositorioRestricao;

public class RepositorioRestricaoSQL implements RepositorioRestricao {
  
  private Connection conn;
  
  public RepositorioRestricaoSQL(Connection conn) {
    this.conn = conn;
  }

  @Override
  public Restricao buscarPeloId(int id) {
    // TODO: a principio isso não é necessário, mas se for é só implementar...
    throw new RuntimeException("nao implementado");
  }

  @Override
  public void cadastrar(Restricao restricao) {
    String sql = 
        "INSERT INTO restricao (id_usuario, data_inicio, data_fim, motivo) " + 
        "VALUES (?, ?, ?, ?) RETURNING id_restricao";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, restricao.getUsuario().getId());
      st.setDate(2, DateUtil.convertLocalDateToDate(restricao.getDataInicio()));
      st.setDate(3, DateUtil.convertLocalDateToDate(restricao.getDataFim()));
      st.setString(4, restricao.getMotivo());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      restricao.setId(rs.getInt("id_restricao"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM restricao WHERE id_restricao = ?")) { //  RETURNING id_autor, nome;
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Restricao buscarRestricaoAtiva(Usuario usuario) {
    String sql = 
        "SELECT id_restricao, data_inicio, data_fim, motivo " + 
        "FROM restricao WHERE id_usuario = ? AND data_fim > now()";
      
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, usuario.getId());
      
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        Restricao restricao = new Restricao();
        restricao.setId(rs.getInt("id_restricao"));
        restricao.setUsuario(usuario);
        restricao.setDataInicio(DateUtil.convertDateToLocalDate(rs.getDate("data_inicio")));
        restricao.setDataFim(DateUtil.convertDateToLocalDate(rs.getDate("data_fim")));
        restricao.setMotivo(rs.getString("motivo"));
        return restricao;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }
  
  @Override
  public void atualizar(Restricao restricao) {
    String sql = 
        "UPDATE restricao SET id_usuario = ?, data_inicio = ?, data_fim = ?, motivo = ? "+ 
        "WHERE id_restricao = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {  
      st.setInt(1, restricao.getUsuario().getId());
      st.setDate(2, DateUtil.convertLocalDateToDate(restricao.getDataInicio()));
      st.setDate(3, DateUtil.convertLocalDateToDate(restricao.getDataFim()));
      st.setString(4, restricao.getMotivo());
      st.setInt(5, restricao.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
}
