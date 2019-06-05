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
	
	public void onMenuItemCadastrarUsuario() {
		loadView("/gui/CadastrarUsuarioView.fxml");
	}
	
	public void onMenuItemRemoverUsuario() {
		loadView("/gui/RemoverUsuarioView.fxml");
	}
	
	public void onMenuItemCadastrarLivro() {
		loadView("/gui/CadastrarLivroView.fxml");
	}
	
	public void onMenuItemRemoverLivro() {
		loadView("/gui/RemoverLivroView.fxml");
	}
	
	public void onMenuItemAdicionarEmprestimo() {
		loadView("/gui/AdicionarEmprestimoView.fxml");
	}
	
	public void onMenuItemAbaterEmprestimo() {
		loadView("/gui/AbaterEmprestimoView.fxml");
	}
	
	public void onMenuItemAdicionarRestricao() {
		loadView("/gui/AdicionarRestricaoView.fxml");
	}
	
	public void onMenuItemRemoverRestricao() {
		loadView("/gui/RemoverRestricaoView.fxml");
	}
	
	public void onMenuItemConsultaDeLivro() {
		loadView("/gui/ConsultarLivroView.fxml");
	}
	
	public void onMenuItemConsultaDeEmprestimo() {
		loadView("/gui/ConsultarEmprestimoView.fxml");
	}

	public void onMenuItemCadastrarExemplar() {
	  loadView("/gui/CadastrarExemplarView.fxml");
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
			e.printStackTrace();
		}
	}
	
}
