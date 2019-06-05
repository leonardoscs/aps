package gui;


import application.Main;
import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ConsultarEmprestimoController implements Initializable {

	@FXML
	private TextField fieldMatricula;
	
	@FXML
	private TableView<Emprestimo> tabelaEmprestimos;
	
	@FXML
  private TableColumn<Emprestimo, String> tituloCol;
  
	@FXML
  private TableColumn<Emprestimo, String> codExemplarCol;
	
	@FXML
  private TableColumn<Emprestimo, String> dataEmprestimoCol;
	
	@FXML
  private TableColumn<Emprestimo, String> dataDevolucaoCol;

	public void onBtConsultar() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
		RepositorioUsuario repoUsuario = repos.getRepositorio(RepositorioUsuario.class);
		RepositorioEmprestimo repoEmp = repos.getRepositorio(RepositorioEmprestimo.class);

		Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(fieldMatricula.getText()));

		if (usuario == null) {
			Alerts.showAlert("Erro", null, "Não existe um usuário com a matrícula: " + fieldMatricula.getText(),
				Alert.AlertType.ERROR);
			return;
		}

		List<Emprestimo> emprestimos = repoEmp.buscarEmprestimos(usuario);

		if (emprestimos.size() == 0) {
			Alerts.showAlert("Aviso", null, "O usuário não possui nenhum emprestimo cadastrado.", Alert.AlertType.INFORMATION);
			return;
		}

		tabelaEmprestimos.setItems(FXCollections.observableArrayList(emprestimos));
	}

	private boolean validaCampos() {
		try {
			ValidadorCampo.valida(fieldMatricula, "Matrícula", Validadores.NAO_VAZIO, Validadores.NUMERO_LONG);
			return true;
		} catch (RuntimeException ex) {
			Alerts.showAlert("Aviso", null, ex.getMessage(), Alert.AlertType.WARNING);
			return false;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		final ReadOnlyStringWrapper strAtivo = new ReadOnlyStringWrapper("Ativo");
		final DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		tituloCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getExemplar().getLivro().getTitulo()));
		codExemplarCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(Integer.toString(f.getValue().getExemplar().getId())));
		dataEmprestimoCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(df.format(f.getValue().getDataEmprestou())));
		dataDevolucaoCol.setCellValueFactory(f -> {
			if (f.getValue().getDataDevolveu() == null) {
				return strAtivo;
			}
			return new ReadOnlyStringWrapper(df.format(f.getValue().getDataDevolveu()));
		});
	}
}
