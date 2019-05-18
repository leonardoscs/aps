package biblioteca.entidades;

public class TipoUsuario {

  private int id;
  private String descricao;
  private int quantidadeDiasEmprestimo;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDescricao() {
    return descricao;
  }

  public void setDescricao(String descricao) {
    this.descricao = descricao;
  }

  public int getQuantidadeDiasEmprestimo() {
    return quantidadeDiasEmprestimo;
  }

  public void setQuantidadeDiasEmprestimo(int quantidadeDiasEmprestimo) {
    this.quantidadeDiasEmprestimo = quantidadeDiasEmprestimo;
  }

  @Override
  public String toString() {
    return "TipoUsuario [id=" + id + ", descricao=" + descricao + ", quantidadeDiasEmprestimo=" + quantidadeDiasEmprestimo + "]";
  }
  
}
