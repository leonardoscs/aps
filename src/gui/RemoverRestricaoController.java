package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RemoverRestricaoController {

	@FXML
	private TextField matricula;
	
	@FXML
	private TextField motivo;
	
	@FXML
	private Button removerRestricao;
	
	public void onBtRemoverRestricao() {
		//comandos para remover a restricao do BD
		Alerts.showAlert("Aviso", null, "Restrição removida com sucesso", AlertType.CONFIRMATION);
	}
	
}
