package biblioteca.entidades;

public class ExemplarLivro {

  private int id;
  private Livro livro;
  private boolean disponivel = true; // Por padrão está disponível
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public Livro getLivro() {
    return livro;
  }
  
  public void setLivro(Livro livro) {
    this.livro = livro;
  }
  
  public boolean estaDisponivel() {
    return disponivel;
  }
  
  public void setDisponivel(boolean disponivel) {
    this.disponivel = disponivel;
  }

  @Override
  public String toString() {
    return "ExemplarLivro [id=" + id + ", livro=" + livro + ", disponivel=" + disponivel + "]";
  }
  
}
