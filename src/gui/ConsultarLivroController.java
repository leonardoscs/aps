package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConsultarLivroController {

	@FXML
	private TextField nome;
	
	@FXML
	private Button consultar;
	
	public void onBtConsultar() {
		//comandos
		Alerts.showAlert("Aviso", null, "Livro disponível", AlertType.CONFIRMATION);
		Alerts.showAlert("Aviso", null, "O livro não está disponível", AlertType.WARNING);
	}
	
}
