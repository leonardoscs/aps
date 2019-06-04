package gui;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AdicionarEmprestimoController {

	LocalDate dataAtual = LocalDate.now();
	
	LocalDate dataAux = dataAtual.plusDays(7);
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	
	Date dataEntrega = Date.from(dataAux.atStartOfDay(ZoneId.systemDefault()).toInstant());
	
	@FXML
	private TextField matriculaUsuario;
	
	@FXML
	private TextField nomeLivro;
	
	@FXML
	private Button cadastrarEmprestimo;
	
	public void onBtCadastrarEmprestimo() {
		//comandos para cadastrar o emprestimo
		Alerts.showAlert("Aviso", null, "Empréstimo cadastrado, o usuário deve devolver o livro até o dia " + sdf.format(dataEntrega), AlertType.CONFIRMATION);
	}
	
}
