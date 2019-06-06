package biblioteca.repositorio.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.entidades.Editora;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.RepositorioLivro;

public class RepositorioLivroSQL implements RepositorioLivro {

  private Connection conn;
  
  public RepositorioLivroSQL(Connection conn) {
    this.conn = conn;
  }
  
  @Override
  public Livro buscarPeloId(int id) {
    // Query que só retorna as informações do livro e da editora.
    String sql = 
        "SELECT livro.id_livro, titulo, descricao, qtd_paginas, data_publicacao, " + 
        "    localizacao, editora.nome as \"nome_editora\", editora.id_editora " + 
        "    FROM livro " + 
        "    JOIN editora ON (livro.id_editora = editora.id_editora) " + 
        "    WHERE id_livro = ?";
    
   
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, id);
      
      ResultSet rs = st.executeQuery();
      
      if (rs.next()) { // Livro existe
        return livroFromResultSet(rs);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  // TODO: Talvez isso devesse ficar no RepositorioAutor?
  private List<Autor> pegarAutores(int idLivro) throws SQLException {
    String sql = 
        " SELECT                                     " +
        "   la.id_autor,                             " +
        "   nome                                     " +
        " FROM                                       " +
        "   livro_autor la                           " +
        " JOIN                                       " +
        "   autor ON (autor.id_autor = la.id_autor)  " +
        " WHERE                                      " +
        "   la.id_livro = ?                          " ;
    
    List<Autor> autores = new ArrayList<>();
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, idLivro);
      
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        autores.add(new Autor(rs.getInt("id_autor"), rs.getString("nome")));
      }
    }
    
    return autores;
  }
  
  private List<Categoria> pegarCategorias(int idLivro) throws SQLException {
    String sql = 
        " SELECT                                     " +
        "   lc.id_categoria,                         " +
        "   nome                                     " +
        " FROM                                       " +
        "   livro_categoria lc                       " +
        " JOIN                                       " +
        "   categoria cat ON (cat.id_categoria = lc.id_categoria)  " +
        " WHERE                                      " +
        "   lc.id_livro = ?                          " ;
    
    List<Categoria> autores = new ArrayList<>();
    
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, idLivro);
      
      ResultSet rs = st.executeQuery();
      while (rs.next()) {
        autores.add(new Categoria(rs.getInt("id_categoria"), rs.getString("nome")));
      }
    }
    
    return autores;
  }
  
  private void cadastra(Livro livro) throws SQLException {
    conn.setAutoCommit(false);
    
    final String sql = 
        "INSERT INTO livro (" + 
        "  titulo, descricao, qtd_paginas, data_publicacao, " +
        "  localizacao, id_editora " + 
        ") VALUES (?, ?, ?, ?, ?, ?) RETURNING id_livro";
  
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, livro.getTitulo());
      st.setString(2, livro.getDescricao());
      st.setInt(3, livro.getQuantidadePaginas());
      st.setDate(4, DateUtil.convertLocalDateToDate(livro.getDataPublicacao()));
      st.setString(5, livro.getLocalizacao());
      st.setInt(6, livro.getEditora().getId());
      
      ResultSet rs = st.executeQuery();
      rs.next();
      
      livro.setId(rs.getInt("id_livro"));
    }
    
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO livro_autor (id_livro, id_autor) VALUES (?, ?)")) {
      for (Autor autor : livro.getAutores()) {
        st.setInt(1, livro.getId());
        st.setInt(2, autor.getId());
        st.addBatch();
      }
      st.executeBatch();
    }
    
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO livro_categoria (id_livro, id_categoria) VALUES (?, ?)")) {
      for (Categoria cat : livro.getCategorias()) {
        st.setInt(1, livro.getId());
        st.setInt(2, cat.getId());
        st.addBatch();
      }
      st.executeBatch();
    }
    
    // Restaura o autocommit (isso já faz o commit automaticamente)
    conn.setAutoCommit(true);
  }
  
  @Override
  public void cadastrar(Livro livro) {
    // TODO: fazer a verificação se tudo que está sendo inserido é valido.
    // Por exemplo: verificar campos non null, verificar autores
    try {
      cadastra(livro);
    } catch (SQLException ex) {
      try {
        conn.rollback();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void deletarPeloId(int id) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro WHERE id_livro = ?")) {
      st.setInt(1, id);
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void atualizar(Livro livro) {
    // A principio isso só ira atualizar as informações do livro. 
    // Categorias e Autores serão atualizados com o uso dos métodos
    // adicionar/remover autor/categoria
    
    final String sql = 
        "UPDATE livro SET titulo = ?, descricao = ?, qtd_paginas = ?, "+
        " data_publicacao = ?, localizacao = ?, id_editora = ? WHERE id_livro = ?";
  
    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, livro.getTitulo());
      st.setString(2, livro.getDescricao());
      st.setInt(3, livro.getQuantidadePaginas());
      st.setDate(4, DateUtil.convertLocalDateToDate(livro.getDataPublicacao()));
      st.setString(5, livro.getLocalizacao());
      st.setInt(6, livro.getEditora().getId());
      
      st.setInt(7, livro.getId());
      
      st.executeUpdate();
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void adicionarAutor(Livro livro, Autor autor) {
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO livro_autor (id_livro, id_autor) VALUES (?, ?)")) {
      st.setInt(1, livro.getId());
      st.setInt(2, autor.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public void removerAutor(Livro livro, Autor autor) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_autor WHERE id_livro = ? AND id_autor = ?")) {
      st.setInt(1, livro.getId());
      st.setInt(2, autor.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public void adicionarCategoria(Livro livro, Categoria categoria) {
    // TODO: adicionar categoria a lista livro.categorias aqui?
    try (PreparedStatement st = conn.prepareStatement("INSERT INTO livro_categoria (id_livro, id_categoria) VALUES (?, ?)")) {
      st.setInt(1, livro.getId());
      st.setInt(2, categoria.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public void removerCategoria(Livro livro, Categoria categoria) {
    try (PreparedStatement st = conn.prepareStatement("DELETE FROM livro_categoria WHERE id_livro = ? AND id_categoria = ?")) {
      st.setInt(1, livro.getId());
      st.setInt(2, categoria.getId());
      st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException();
    }
  }

  @Override
  public List<Livro> buscarPeloTituloParcial(String titulo) {
    String sql =
      "SELECT livro.id_livro, titulo, descricao, qtd_paginas, data_publicacao, " +
        "    localizacao, editora.nome as nome_editora, editora.id_editora " +
        "    FROM livro " +
        "    JOIN editora ON (livro.id_editora = editora.id_editora) " +
        "    WHERE lower(titulo) LIKE lower('%' || ? || '%')";

    List<Livro> resultados = new ArrayList<>();

    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setString(1, titulo);

      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        Livro livro = livroFromResultSet(rs);
        resultados.add(livro);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return resultados;
  }

  @Override
  public List<Livro> buscarPeloAutor(Autor autor) {
    String sql =
      "SELECT livro.id_livro, titulo, descricao, qtd_paginas, data_publicacao, " +
        "    localizacao, editora.nome as nome_editora, editora.id_editora " +
        "    FROM livro " +
        "    JOIN editora ON (livro.id_editora = editora.id_editora) " +
        "    WHERE id_livro IN (select id_livro from livro_autor where id_autor = ?)";

    List<Livro> resultados = new ArrayList<>();

    try (PreparedStatement st = conn.prepareStatement(sql)) {
      st.setInt(1, autor.getId());

      ResultSet rs = st.executeQuery();

      while (rs.next()) {
        Livro livro = livroFromResultSet(rs);
        resultados.add(livro);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return resultados;
  }

  // Método utilitário que cria um livro a partir do ResultSet
  private Livro livroFromResultSet(ResultSet rs) throws SQLException {
    int idLivro = rs.getInt("id_livro");

    Editora editora = new Editora(rs.getInt("id_editora"), rs.getString("nome_editora"));

    Livro livro = new Livro();
    livro.setId(idLivro);
    livro.setTitulo(rs.getString("titulo"));
    livro.setDescricao(rs.getString("descricao"));
    livro.setLocalizacao(rs.getString("localizacao"));
    livro.setQuantidadePaginas(rs.getShort("qtd_paginas"));
    livro.setDataPublicacao(DateUtil.convertDateToLocalDate(rs.getDate("data_publicacao")));;
    livro.setEditora(editora);

    livro.setAutores(pegarAutores(idLivro));
    livro.setCategorias(pegarCategorias(idLivro));

    return livro;
  }

}
