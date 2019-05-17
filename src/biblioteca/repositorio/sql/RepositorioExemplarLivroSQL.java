package biblioteca.repositorio.sql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioLivro;

public class RepositorioExemplarLivroSQL implements RepositorioExemplarLivro {
  
  private Connection conn;
  private RepositorioLivro repoLivro;
  
  public RepositorioExemplarLivroSQL(Connection conn, RepositorioLivro repoLivro) {
    this.conn = conn;
    this.repoLivro = repoLivro;
  }

  @Override
  public ExemplarLivro buscarPeloId(int id) {
    String sql = "SELECT id_exemplar, id_livro, disponivel FROM exemplar_livro WHERE id_exemplar = ?";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      if (rs.next()) {
        Livro livro = repoLivro.buscarPeloId(rs.getInt("id_livro"));
        
        ExemplarLivro exemplar = new ExemplarLivro(); 
        exemplar.setId(rs.getInt("id_exemplar"));
        exemplar.setDisponivel(rs.getBoolean("disponivel"));
        exemplar.setLivro(livro);
        
        return exemplar;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  @Override
  public void cadastrar(ExemplarLivro exemplar) {
    String sql = 
        "INSERT INTO exemplar_livro (id_livro, disponivel) VALUES (?, ?) RETURNING id_exemplar";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, exemplar.getLivro().getId());
      st.setBoolean(2, exemplar.estaDisponivel());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      exemplar.setId(rs.getInt("id_exemplar"));
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM exemplar_livro WHERE id_exemplar = ?")) { //  RETURNING id_autor, nome;
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<ExemplarLivro> buscarExemplares(Livro livro) {
    List<ExemplarLivro> exemplares = new ArrayList<>();
    
    String sql = "SELECT id_exemplar, disponivel FROM exemplar_livro WHERE id_livro = ?";
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, livro.getId());
      
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        ExemplarLivro exemplar = new ExemplarLivro();
        exemplar.setId(rs.getInt("id_exemplar"));
        exemplar.setDisponivel(rs.getBoolean("disponivel"));
        exemplar.setLivro(livro);
        exemplares.add(exemplar);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    
    return exemplares;
  }
  
  @Override
  public void atualizar(ExemplarLivro exemplar) {
    String sql = 
        "UPDATE exemplar_livro SET disponivel = ?, id_livro = ? WHERE id_exemplar = ?";
    try (PreparedStatement st = conn.prepareStatement(sql)) {  
      st.setBoolean(1, exemplar.estaDisponivel());
      st.setInt(2, exemplar.getLivro().getId());
      st.setInt(1, exemplar.getId());

      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
  
}
