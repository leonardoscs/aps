package gui;


import application.Main;
import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.repositorio.RepositorioAutor;
import biblioteca.repositorio.RepositorioCategoria;
import gui.util.Alerts;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CadastrarAutorController implements Initializable {

	@FXML
	private TextField inputNomeFiltro;

	@FXML
	private TableView<Autor> tabelaAutores;
  
	@FXML
  private TableColumn<Autor, String> nomeCol;

	private List<Autor> autoresCarregados;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getNome()));

		atualizaTabela();
	}

	public void onBtnRemoverSelecionado() {
		RepositorioAutor repoAutor = Main.getGerenciadorRepositorio().getRepositorio(RepositorioAutor.class);

		Autor selecionado = tabelaAutores.getSelectionModel().getSelectedItem();

		if (selecionado == null) {
			Alerts.showAlert("Aviso", null, "Nenhum autor selecionado",
				Alert.AlertType.WARNING);
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirme");
		alert.setHeaderText(null);
		alert.setContentText(String.format("Deseja remover o autor '%s'?", selecionado.getNome()));
		alert.getButtonTypes().clear();
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = alert.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		try {
			repoAutor.deletarPeloId(selecionado.getId());

			Alerts.showAlert("Successo", null, "Autor removido com sucesso!",
				Alert.AlertType.INFORMATION);

			tabelaAutores.getItems().remove(tabelaAutores.getSelectionModel().getSelectedIndex());
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao remover o autor:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnAdicionar() {
		RepositorioAutor repoAutor = Main.getGerenciadorRepositorio().getRepositorio(RepositorioAutor.class);

		TextInputDialog input = new TextInputDialog();

		input.setTitle("Adicionar autor");
		input.setHeaderText(null);
		input.setContentText("Nome do autor: ");

		Optional<String> resultado = input.showAndWait();
		if (!resultado.isPresent() || resultado.get().isEmpty()) {
			return;
		}

		String nome = resultado.get();

		if (repoAutor.buscarPeloNome(nome) != null) {
			Alerts.showAlert("Erro", null, "Já existe um autor com este nome!",
				Alert.AlertType.ERROR);
			return;
		}

		Autor autor = new Autor(nome);

		try {
			repoAutor.cadastrar(autor);

			Alerts.showAlert("Successo", null, "Autor cadastrado com sucesso!",
				Alert.AlertType.INFORMATION);

			atualizaTabela();
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao adicionar o autor:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnFiltrar() {
		if (inputNomeFiltro.getText().isEmpty()) {
			tabelaAutores.setItems(FXCollections.observableList(autoresCarregados));
			return;
		}

		String nome = inputNomeFiltro.getText().toLowerCase();
		String[] partes = nome.split(" ");

		Predicate<Autor> filtro = partes.length == 1
			? a -> a.getNome().toLowerCase().contains(partes[0])
			: a -> Arrays.stream(partes).anyMatch(parte -> a.getNome().toLowerCase().contains(parte));

		List<Autor> filtrado = autoresCarregados.stream()
			.filter(filtro)
			.collect(Collectors.toList());

		tabelaAutores.setItems(FXCollections.observableList(filtrado));
	}

	private void atualizaTabela() {
		RepositorioAutor repoAutor = Main.getGerenciadorRepositorio().getRepositorio(RepositorioAutor.class);

		autoresCarregados = repoAutor.buscarTodos();

		tabelaAutores.setItems(FXCollections.observableList(autoresCarregados));
	}
}
