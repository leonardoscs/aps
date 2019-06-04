package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemCadastarUsuario;
	
	@FXML
	private MenuItem menuItemRemoverUsuario;
	
	@FXML
	private MenuItem menuItemCadastrarLivro;
	
	@FXML
	private MenuItem menuItemRemoverLivro;
	
	@FXML
	private MenuItem menuItemAdicionarEmprestimo;
	
	@FXML
	private MenuItem menuItemAbaterEmprestimo;
	
	@FXML
	private MenuItem menuItemAdicionarRestricao;
	
	@FXML
	private MenuItem menuItemRemoverRestricao;
	
	@FXML
	private MenuItem menuItemConsultaDeLivros;

	@FXML
	private MenuItem menuItemConsultaDeEmprestimo;
	
	@FXML
	public void onMenuItemCadastrarUsuario() {
		loadView("/gui/CadastrarUsuario.fxml");
	}
	
	@FXML
	public void onMenuItemRemoverUsuario() {
		System.out.println("a1");
	}
	
	@FXML
	public void onMenuItemCadastrarLivro() {
		System.out.println("b");
	}
	
	@FXML
	public void onMenuItemRemoverLivro() {
		System.out.println("b1");
	}
	
	@FXML
	public void onMenuItemAdicionarEmprestimo() {
		System.out.println("c");
	}
	
	@FXML
	public void onMenuItemAbaterEmprestimo() {
		System.out.println("d");
	}
	
	@FXML
	public void onMenuItemAdicionarRestricao() {
		System.out.println("e");
	}
	
	@FXML
	public void onMenuItemRemoverRestricao() {
		System.out.println("f");
	}
	
	@FXML
	public void onMenuItemConsultaDeLivro() {
		System.out.println("g");
	}
	
	@FXML
	public void onMenuItemConsultaDeEmprestimo() {
		System.out.println("h");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView(String caminhoDaView) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			
			mainVBox.getChildren().addAll(newVBox.getChildren());
		
		} catch (IOException e) {
			Alerts.showAlert("Algo deu errado meu consagrado", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
}
