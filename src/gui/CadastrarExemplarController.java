package gui;


import application.Main;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.*;
import gui.util.Alerts;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CadastrarExemplarController implements Initializable {

	@FXML
	private TextField fieldCodLivro;
	
	@FXML
	private TableView<ExemplarLivro> tabelaExemplares;
  
	@FXML
  private TableColumn<ExemplarLivro, String> codExemplarCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		codExemplarCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(Integer.toString(f.getValue().getId())));
	}

	public void onBtnRemoverSelecionado() {
		RepositorioExemplarLivro repoExemplar = Main.getGerenciadorRepositorio().getRepositorio(RepositorioExemplarLivro.class);

		ExemplarLivro exemplarSelecionado = tabelaExemplares.getSelectionModel().getSelectedItem();

		if (exemplarSelecionado == null) {
			Alerts.showAlert("Aviso", null, "Nenhum exemplar selecionado",
				Alert.AlertType.WARNING);
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirme");
		alert.setHeaderText(null);
		alert.setContentText(String.format("Deseja remover o exemplar de código %d?", exemplarSelecionado.getId()));
		alert.getButtonTypes().clear();
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		Optional<ButtonType> respostaOpt = alert.showAndWait();
		if (!respostaOpt.isPresent() || respostaOpt.get() == ButtonType.NO) {
			return;
		}

		try {
			repoExemplar.deletarPeloId(exemplarSelecionado.getId());

			Alerts.showAlert("Successo", null, "Exemplar removido com sucesso!",
				Alert.AlertType.INFORMATION);

			// Atualiza lista de exemplares
			tabelaExemplares.getItems().remove(tabelaExemplares.getSelectionModel().getSelectedIndex());
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao adicionar o exemplar:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnAdicionar() {
		if (!validaCampos()) {
			return;
		}

		RepositorioExemplarLivro repoExemplar = Main.getGerenciadorRepositorio().getRepositorio(RepositorioExemplarLivro.class);
		RepositorioLivro repoLivro = Main.getGerenciadorRepositorio().getRepositorio(RepositorioLivro.class);

		Livro livro = repoLivro.buscarPeloId(Integer.parseInt(fieldCodLivro.getText()));

		if (livro == null) {
			Alerts.showAlert("Erro", null, "Não existe um livro com o código: " + fieldCodLivro.getText(),
				Alert.AlertType.ERROR);
			return;
		}

		ExemplarLivro exemplar = new ExemplarLivro();
		exemplar.setDisponivel(true);
		exemplar.setLivro(livro);

		try {
			repoExemplar.cadastrar(exemplar);

			Alerts.showAlert("Successo", null, "Novo exemplar adicionado. Código: " + exemplar.getId(),
				Alert.AlertType.INFORMATION);

			// Atualiza lista de exemplares
			tabelaExemplares.setItems(FXCollections.observableList(repoExemplar.buscarExemplares(livro)));
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao adicionar o exemplar:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnBuscar() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
		RepositorioExemplarLivro repoExemplar = repos.getRepositorio(RepositorioExemplarLivro.class);
		RepositorioLivro repoLivro = repos.getRepositorio(RepositorioLivro.class);

		Livro livro = repoLivro.buscarPeloId(Integer.parseInt(fieldCodLivro.getText()));

		if (livro == null) {
			Alerts.showAlert("Erro", null, "Não existe um livro com o código: " + fieldCodLivro.getText(),
				Alert.AlertType.ERROR);
			return;
		}

		List<ExemplarLivro> exemplares = repoExemplar.buscarExemplares(livro);

		if (exemplares.size() == 0) {
			Alerts.showAlert("Aviso", null, "O livro informado não possui exemplares cadastrados.",
				Alert.AlertType.INFORMATION);
			return;
		}

		tabelaExemplares.setItems(FXCollections.observableList(exemplares));
	}

	private boolean validaCampos() {
		try {
			ValidadorCampo.valida(fieldCodLivro, "Código do Livro", Validadores.NAO_VAZIO, Validadores.NUMERO_INT);
			return true;
		} catch (RuntimeException ex) {
			Alerts.showAlert("Aviso", null, ex.getMessage(), Alert.AlertType.WARNING);
			return false;
		}
	}
}
