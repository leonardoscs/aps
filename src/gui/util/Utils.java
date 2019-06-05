package gui.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

public class Utils {

  public static Stage currentStage(ActionEvent event) {
    return (Stage) ((Node)event.getSource()).getScene().getWindow();
  }

  public static Integer tryParseToInt(String str) {
    return Integer.parseInt(str);
  }

  // Limpa os campos dinamicamente usando reflection
  public static void limpaCamposDinamicamente(Object obj) {
    Field[] fields = obj.getClass().getDeclaredFields();

    Arrays.stream(fields)
      .filter(f -> f.isAnnotationPresent(FXML.class))
      .map(f -> {
        try {
          f.setAccessible(true);
          return f.get(obj);
        } catch (IllegalAccessException e) {
          e.printStackTrace();
          return null;
        }
      })
      .filter(Objects::nonNull)
      .forEach(f -> {
        if (f instanceof TextField){
          ((TextField) f).setText("");
        } else if (f instanceof TextArea) {
          ((TextArea) f).setText("");
        } else if (f instanceof ChoiceBox) {
          ((ChoiceBox) f).getSelectionModel().clearSelection();
        }
      });
  }
}
