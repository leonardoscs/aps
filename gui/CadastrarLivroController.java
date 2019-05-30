package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CadastrarLivroController implements Initializable{
	
	int x = 0;
	
	@FXML
	private TextField titulo;
	
	@FXML
	private TextField descricao;
	 
	@FXML
	private TextField qtd_paginas;
	
	@FXML
	private TextField data_publicacao;
	
	@FXML
	private TextField localizacao;
	
	@FXML
	private TextField editora;
	
	@FXML
	private TextField categoria;
	
	@FXML
	private TextField autor;
	
	@FXML
	private Button cadastrar;
	
	public void onBtCadastrar() {
		if(titulo.getText().isEmpty()) {
			x++;
		} 
		if(descricao.getText().isEmpty()) {
			x++;
		} 
		if(data_publicacao.getText().isEmpty()) {
			x++;
		} 
		if(localizacao.getText().isEmpty()) {
			x++;
		} 
		if(qtd_paginas.getText().isEmpty()) {
			x++;
		} 
		if(editora.getText().isEmpty()) {
			x++;
		}
		if(categoria.getText().isEmpty()) {
			x++;
		}
		if(autor.getText().isEmpty()) {
			x++;
		}
		
		if(x == 0) {
			//comando para inserir dados do tipo
			//comando para inserir dados do usuario
			Alerts.showAlert("Aviso", null, "Livro cadastrado", AlertType.CONFIRMATION);
		} else {
			Alerts.showAlert("Aviso", null, "Todos os campos precisam ser preenchidos", AlertType.WARNING);
			x = 0;
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	
}
