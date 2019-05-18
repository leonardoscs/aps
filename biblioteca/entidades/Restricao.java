package biblioteca.entidades;

import java.time.LocalDate;

public class Restricao {
  
  private int id;
  private Usuario usuario;
  private LocalDate dataInicio;
  private LocalDate dataFim;
  private String motivo;
  
  public Usuario getUsuario() {
    return usuario;
  }
  
  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  
  public LocalDate getDataInicio() {
    return dataInicio;
  }
  
  public void setDataInicio(LocalDate dataInicio) {
    this.dataInicio = dataInicio;
  }
  
  public LocalDate getDataFim() {
    return dataFim;
  }
  
  public void setDataFim(LocalDate dataFim) {
    this.dataFim = dataFim;
  }
  
  public String getMotivo() {
    return motivo;
  }
  
  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  @Override
  public String toString() {
    return "Restricao [usuario=" + usuario + ", dataInicio=" + dataInicio + ", dataFim=" + dataFim + ", motivo=" + motivo + "]";
  }
  
}
