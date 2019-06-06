package application;

import java.io.IOException;

import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.sql.GerenciadorRepositorioSQL;
import gui.util.Alerts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {
  
  private static Scene mainScene;
  private static GerenciadorRepositorio gerenciadorRepositorio;
  
  @Override
  public void start(Stage primaryStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
      ScrollPane scrollPane = loader.load();

      scrollPane.setFitToHeight(true);
      scrollPane.setFitToWidth(true);

      mainScene = new Scene(scrollPane);
      primaryStage.setWidth(700);
      primaryStage.setHeight(500);
      primaryStage.setResizable(false);
      primaryStage.setScene(mainScene);
      primaryStage.setTitle("Biblioteca IES");
      primaryStage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      inicializarGerenciadorRepositorio();
    } catch (Exception ex) {
      Alerts.showAlert("Erro", "Não foi possível inicializar os repositórios.", ex.getMessage(), AlertType.ERROR);
      ex.printStackTrace();
    }
  }
  
  private void inicializarGerenciadorRepositorio() throws Exception {
    // TODO: quando a implementação em arquivo foi feita, terá que ter algo
    // para selecionar entre SQL e arquivo...
    // Por agora, apenas a implementação em SQL existe.
    gerenciadorRepositorio = new GerenciadorRepositorioSQL("jdbc:postgresql://localhost:5432/biblioteca",
      "aps", "123");
    gerenciadorRepositorio.inicializar();
  }
  
  public static GerenciadorRepositorio getGerenciadorRepositorio() {
    return gerenciadorRepositorio;
  }
  
  public static Scene getMainScene() {
    return mainScene;
  }
  
  public static void main(String[] args) {
    launch(args);
  }
}
