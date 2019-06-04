package biblioteca.entidades;

import java.time.LocalDate;

public class Emprestimo {

  private int id;
  private Usuario usuario;
  private ExemplarLivro exemplar;
  private LocalDate dataEmprestou;
  private LocalDate dataDevolveu;
  private LocalDate dataLimiteDevolucao;
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public Usuario getUsuario() {
    return usuario;
  }
  
  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  
  public ExemplarLivro getExemplar() {
    return exemplar;
  }
  
  public void setExemplar(ExemplarLivro exemplar) {
    this.exemplar = exemplar;
  }
  
  public LocalDate getDataEmprestou() {
    return dataEmprestou;
  }
  
  public void setDataEmprestou(LocalDate dataEmprestou) {
    this.dataEmprestou = dataEmprestou;
  }
  
  public LocalDate getDataDevolveu() {
    return dataDevolveu;
  }
  
  public void setDataDevolveu(LocalDate dataDevolveu) {
    this.dataDevolveu = dataDevolveu;
  }
  
  public LocalDate getDataLimiteDevolucao() {
    return dataLimiteDevolucao;
  }
  
  public void setDataLimiteDevolucao(LocalDate dataLimiteDevolucao) {
    this.dataLimiteDevolucao = dataLimiteDevolucao;
  }

  @Override
  public String toString() {
    return "Emprestimo [id=" + id + ", usuario=" + usuario + ", exemplar=" + exemplar + ", dataEmprestou=" + dataEmprestou + ", dataDevolveu="
        + dataDevolveu + ", dataLimiteDevolucao=" + dataLimiteDevolucao + "]";
  }
  
}
