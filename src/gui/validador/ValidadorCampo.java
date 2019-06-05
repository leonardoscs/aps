package gui.validador;

import javafx.scene.control.Control;

public class ValidadorCampo {

  /**
   * Aplica todos os validadores
   * @throws RuntimeException lança caso algum validador retorne invalido.
   */
  public static void valida(Control controle, String nomeControle, Validador ... validadores) {
    for (Validador validador : validadores) {
      String resultado = validador.valida(controle, nomeControle);

      if (resultado != null) {
        throw new RuntimeException(resultado);
      }
    }
  }

}
