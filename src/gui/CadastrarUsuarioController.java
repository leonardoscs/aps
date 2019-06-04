package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CadastrarUsuarioController implements Initializable{

	int x = 0;
	
	@FXML
	private TextField nome;
	
	@FXML
	private TextField matricula;
	
	@FXML
	private TextField telefone;
	
	@FXML
	private TextField email;
	
	@FXML
	private TextField tipo;
	
	@FXML
	private Button cadastrar;
	
	@FXML
	private Label semNome;
	
	@FXML
	private Label semTipo;
	
	@FXML
	private Label semEmail;
	
	@FXML
	private Label semMatricula;
	
	@FXML
	private Label semTelefone;
	
	public void onBtCadastrar() {
		if(matricula.getText().isEmpty()) {
			semMatricula.setText("Campo nome n�o pode estar vazio");
			x++;
		} 
		if(tipo.getText().isEmpty()) {
			semTipo.setText("Campo nome n�o pode estar vazio");
			x++;
		} 
		if(telefone.getText().isEmpty()) {
			semTelefone.setText("Campo nome n�o pode estar vazio");
			x++;
		} 
		if(nome.getText().isEmpty()) {
			semNome.setText("Campo nome n�o pode estar vazio");
			x++;
		} 
		if(email.getText().isEmpty()) {
			semEmail.setText("Campo nome n�o pode estar vazio");
			x++;
		} 
		
		if(x == 0) {
			//comando para inserir dados do tipo
			//comando para inserir dados do usuario
			Alerts.showAlert("Aviso", null, "Usu�rio cadastrado", AlertType.CONFIRMATION);
		} else {
			x = 0;
		}
	}
	
	@Override
	public void initialize(URL url, ResourceBundle arb) {
	}

	
	
	
	
}
