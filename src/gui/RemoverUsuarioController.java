package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RemoverUsuarioController {

	@FXML
	private TextField nome;
	
	@FXML
	private TextField matricula;
	
	@FXML
	private Button remover;
	
	@FXML
	private void onBtRemover() {
		if(nome.getText().isEmpty() && matricula.getText().isEmpty()) {
			Alerts.showAlert("Aviso", null, "É preciso prencher pelo menos um dos campos", AlertType.ERROR);
		} else if(nome.getText().isEmpty()){
			//remover por matricula
			Alerts.showAlert("Aviso", null, "Usuário removido", AlertType.CONFIRMATION);
		} else if(matricula.getText().isEmpty()) {
			//remover por nome
			Alerts.showAlert("Aviso", null, "Usuário removido", AlertType.CONFIRMATION);
		}
	}
	
}
