package gui.validador;

import javafx.scene.control.Control;

public interface Validador {

  /**
   * @return Retorna nulo caso o campo seja valido, caso contrário
   * retorna uma mensagem do motivo de não ser válido.
   */
  String valida(Control control, String nomeControle);

}
