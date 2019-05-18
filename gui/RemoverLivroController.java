package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RemoverLivroController {

	@FXML
	private TextField nome;
	
	@FXML
	private Label aviso;
	
	@FXML
	private Button remover;
	
	public void onBtRemover() {
		if(nome.getText().isEmpty()) {
			aviso.setText("Digite o nome");
		} else {
			//comandos para remover
			Alerts.showAlert("Aviso", null, "Livro removido", AlertType.CONFIRMATION);
		}
	}
	
}
