package gui;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class LoginController {

	@FXML
	private TextField txtlogin;
	
	@FXML
	private TextField txtsenha;
	
	@FXML
	private Label acessoNegado;
	
	@FXML
	private Button login;
	
	public void onBtLoginAcction() {
		
		if(txtlogin.getText().equals("admin") && txtsenha.getText().equals("admin")) {
			Alerts.showAlert("Aviso", null, "Acesso permitido", AlertType.CONFIRMATION);
		} else {
			acessoNegado.setText("Login ou senha inválidos!");
		}
	}
	
}
