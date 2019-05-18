package biblioteca.entidades;

import java.time.LocalDate;
import java.util.List;

public class Livro {

  private int id;
  private String titulo;
  private String descricao;
  private int quantidadePaginas;
  private LocalDate dataPublicacao;
  private String localizacao;
  private List<Autor> autores;
  private List<Categoria> categorias;
  private Editora editora;
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getTitulo() {
    return titulo;
  }
  
  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }
  
  public String getDescricao() {
    return descricao;
  }
  
  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }
  
  public int getQuantidadePaginas() {
    return quantidadePaginas;
  }
  public void setQuantidadePaginas(int quantidadePaginas) {
    this.quantidadePaginas = quantidadePaginas;
  }
  
  public LocalDate getDataPublicacao() {
    return dataPublicacao;
  }
  
  public void setDataPublicacao(LocalDate dataPublicacao) {
    this.dataPublicacao = dataPublicacao;
  }
  
  public String getLocalizacao() {
    return localizacao;
  }

  public void setLocalizacao(String localizacao) {
    this.localizacao = localizacao;
  }
  
  public Editora getEditora() {
    return editora;
  }

  public void setEditora(Editora editora) {
    this.editora = editora;
  }
  
  public List<Autor> getAutores() {
    return autores;
  }
  
  public void setAutores(List<Autor> autores) {
    this.autores = autores;
  }
  
  public List<Categoria> getCategorias() {
    return categorias;
  }
  
  public void setCategorias(List<Categoria> categorias) {
    this.categorias = categorias;
  }

  @Override
  public String toString() {
    return "Livro [id=" + id + ", titulo=" + titulo + ", descricao=" + descricao + ", quantidadePaginas=" + quantidadePaginas + ", dataPublicacao="
        + dataPublicacao + ", localizacao=" + localizacao + ", autores=" + autores + ", categorias=" + categorias + ", editora=" + editora + "]";
  }

}
