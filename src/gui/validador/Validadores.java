package gui.validador;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validadores {

  public static final Validador NAO_VAZIO = (controle, nomeCampo) -> {
    boolean valido = true;
    if (controle instanceof TextField) {
      valido = !((TextField) controle).getText().isEmpty();
    } else if (controle instanceof TextArea) {
      valido = !((TextArea) controle).getText().isEmpty();
    } else if (controle instanceof ChoiceBox) {
      valido = ((ChoiceBox) controle).getSelectionModel().getSelectedItem() != null;
    } else {
      throw new RuntimeException("controle não suportado " + controle);
    }
    return valido ? null : "O campo " + nomeCampo + " deve ser preenchido!";
  };

  public static final Validador NUMERO_LONG = (controle, nomeCampo) -> {
    // Por agora, apenas TextField é suportado
    if (!(controle instanceof TextField)) {
      throw new RuntimeException("controle não suportado " + controle);
    }

    try {
      Long.parseLong(((TextField) controle).getText());
    } catch (NumberFormatException ex) {
      return "O campo " + nomeCampo + " não é um número válido";
    }
    return null;
  };

  public static final Validador NUMERO_INT = (controle, nomeCampo) -> {
    // Por agora, apenas TextField é suportado
    if (!(controle instanceof TextField)) {
      throw new RuntimeException("controle não suportado " + controle);
    }

    try {
      Integer.parseInt(((TextField) controle).getText());
    } catch (NumberFormatException ex) {
      return "O campo " + nomeCampo + " não é um número válido";
    }
    return null;
  };

  // Valida datas no formato dd/mm/yyyy
  public static final Validador DATA = (controle, nomeCampo) -> {
    // Por agora, apenas TextField é suportado
    if (!(controle instanceof TextField)) {
      throw new RuntimeException("controle não suportado " + controle);
    }
    try {
      DateTimeFormatter.ofPattern("d/M/yyyy").parse(((TextField) controle).getText());
    } catch (DateTimeParseException e) {
      e.printStackTrace();
      return "O campo " + nomeCampo + " não é uma data no formato: dia/mês/ano";
    }

    return null;
  };

}
