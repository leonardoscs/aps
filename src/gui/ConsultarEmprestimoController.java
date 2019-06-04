package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConsultarEmprestimoController {

	@FXML
	private TextField nome;
	
	@FXML
	private Button consultar;
	
	public void onBtConsultar() {
		//comandos
		Alerts.showAlert("Aviso", null, "O usu�rio n�o possui empr�stimo pendente", AlertType.WARNING);
		Alerts.showAlert("Aviso", null, "O usu�rio possui empr�stimo pendente do livro tal para ser entregue no dia tal", AlertType.WARNING);
	}
	
}
