package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdicionarRestricaoController implements Initializable {

	@FXML
	private TextField matricula;
	
	@FXML
	private TextField dataFim;
	
	@FXML
	private TextField motivo;
	
	@FXML
	private Button adicionarRestricao;
	
	
	public void onBtAdicionarRestricao() {
		//comandos para cadastrar restricao
		Alerts.showAlert("Aviso", null, "Restrição adicionada, o usuário não poderá fazer empréstimos até o dia ", AlertType.CONFIRMATION);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
	}

}
