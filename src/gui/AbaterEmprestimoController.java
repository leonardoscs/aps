package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AbaterEmprestimoController implements Initializable{

	@FXML
	private TextField matricula;
	
	@FXML
	private TextField nomeLivro;
	
	@FXML
	private Button abaterEmprestimo;
	
	public void onBtAbaterEmprestimo() {
		//comandos para abater emprestimo
		Alerts.showAlert("Aviso", null, "Empréstimo abatido", AlertType.CONFIRMATION);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}

	
	
}
