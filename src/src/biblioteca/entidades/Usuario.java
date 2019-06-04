package biblioteca.entidades;

public class Usuario {

  private int id;
  private TipoUsuario tipo;
  private String nome;
  private String telefone;
  private String email;
  private long matricula;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public TipoUsuario getTipo() {
    return tipo;
  }

  public void setTipo(TipoUsuario tipo) {
    this.tipo = tipo;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public long getMatricula() {
    return matricula;
  }

  public void setMatricula(long matricula) {
    this.matricula = matricula;
  }

  @Override
  public String toString() {
    return "Usuario [id=" + id + ", tipo=" + tipo + ", nome=" + nome + ", telefone=" + telefone + ", email=" + email + ", matricula=" + matricula
        + "]";
  }
}
