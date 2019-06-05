package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioUsuario;

public class RepositorioEmprestimoSQL implements RepositorioEmprestimo {
  
  private Connection conn;
  private RepositorioExemplarLivro repoExemplarLivro;
  private RepositorioUsuario repoUsuario;
  
  public RepositorioEmprestimoSQL(Connection conn, RepositorioExemplarLivro repoExemplarLivro, RepositorioUsuario repoUsuario) {
    this.conn = conn;
    this.repoExemplarLivro = repoExemplarLivro;
    this.repoUsuario = repoUsuario;
  }

  @Override
  public Emprestimo buscarPeloId(int id) {
    throw new RuntimeException("nao implementado");
  }

  @Override
  public void cadastrar(Emprestimo emprestimo) {
    String sql = 
        "INSERT INTO emprestimo (id_usuario, id_exemplar, data_emprestou, data_devolveu, data_limite_devolucao)" +
        "VALUES (?, ?, ?, ?, ?) RETURNING id_emprestimo";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, emprestimo.getUsuario().getId());
      st.setInt(2, emprestimo.getExemplar().getId());
      st.setDate(3, DateUtil.convertLocalDateToDate(emprestimo.getDataEmprestou()));
      st.setDate(4, DateUtil.convertLocalDateToDate(emprestimo.getDataDevolveu()));
      st.setDate(5, DateUtil.convertLocalDateToDate(emprestimo.getDataLimiteDevolucao()));
      
      ResultSet rs = st.executeQuery();
      rs.next();
      emprestimo.setId(rs.getInt("id_emprestimo"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM emprestimo WHERE id_emprestimo = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void atualizar(Emprestimo emprestimo) {
    String sql = 
        "UPDATE emprestimo SET id_usuario = ?, id_exemplar = ?, data_emprestou = ?, " + 
        "data_devolveu = ?, data_limite_devolucao = ? WHERE id_emprestimo = ?";

    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, emprestimo.getUsuario().getId());
      st.setInt(2, emprestimo.getExemplar().getId());
      st.setDate(3, DateUtil.convertLocalDateToDate(emprestimo.getDataEmprestou()));
      st.setDate(4, DateUtil.convertLocalDateToDate(emprestimo.getDataDevolveu()));
      st.setDate(5, DateUtil.convertLocalDateToDate(emprestimo.getDataLimiteDevolucao()));
      
      st.setInt(6, emprestimo.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void marcarEmprestimoComoDevolvido(Emprestimo emprestimo) {
    String sql = "UPDATE emprestimo SET data_devolveu = now() WHERE id_emprestimo = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, emprestimo.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<Emprestimo> buscarEmprestimos(Usuario usuario) {
    List<Emprestimo> emprestimos = new ArrayList<>();
    String sql = 
        "SELECT id_emprestimo, id_exemplar, id_usuario, data_emprestou, " + 
        "data_devolveu, data_limite_devolucao FROM emprestimo WHERE id_usuario = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, usuario.getId());
      
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        Emprestimo emp = new Emprestimo();
        emp.setId(rs.getInt("id_emprestimo"));
        emp.setDataEmprestou(DateUtil.convertDateToLocalDate(rs.getDate("data_emprestou")));
        emp.setDataDevolveu(DateUtil.convertDateToLocalDate(rs.getDate("data_devolveu")));
        emp.setDataLimiteDevolucao(DateUtil.convertDateToLocalDate(rs.getDate("data_limite_devolucao")));
        
        emp.setExemplar(repoExemplarLivro.buscarPeloId(rs.getInt("id_exemplar")));
        emp.setUsuario(usuario);
        
        emprestimos.add(emp);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return emprestimos;
  }

  @Override
  public Emprestimo buscarEmprestimoAtivoPorExemplar(ExemplarLivro exemplar) {
    String sql = 
        "SELECT id_emprestimo, id_exemplar, id_usuario, data_emprestou, " + 
        "data_devolveu, data_limite_devolucao FROM emprestimo WHERE id_exemplar = ? " +
        "AND data_devolveu IS NULL";
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, exemplar.getId());
      
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        Emprestimo emp = new Emprestimo();
        emp.setId(rs.getInt("id_emprestimo"));
        emp.setDataEmprestou(DateUtil.convertDateToLocalDate(rs.getDate("data_emprestou")));
        emp.setDataDevolveu(DateUtil.convertDateToLocalDate(rs.getDate("data_devolveu")));
        emp.setDataLimiteDevolucao(DateUtil.convertDateToLocalDate(rs.getDate("data_limite_devolucao")));
        
        emp.setExemplar(repoExemplarLivro.buscarPeloId(rs.getInt("id_exemplar")));
        emp.setUsuario(repoUsuario.buscarPeloId(rs.getInt("id_usuario")));
        
        return emp;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

}
