package gui;


import application.Main;
import biblioteca.entidades.Categoria;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioCategoria;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioLivro;
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

public class CadastrarCategoriaController implements Initializable {

	@FXML
	private TableView<Categoria> tabelaCategorias;
  
	@FXML
  private TableColumn<Categoria, String> nomeCategoriaCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeCategoriaCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getNome()));

		RepositorioCategoria repoCategoria = Main.getGerenciadorRepositorio().getRepositorio(RepositorioCategoria.class);
		tabelaCategorias.setItems(FXCollections.observableList(repoCategoria.buscarTodas()));
	}

	public void onBtnRemoverSelecionado() {
		RepositorioCategoria repoCategoria = Main.getGerenciadorRepositorio().getRepositorio(RepositorioCategoria.class);

		Categoria selecionado = tabelaCategorias.getSelectionModel().getSelectedItem();

		if (selecionado == null) {
			Alerts.showAlert("Aviso", null, "Nenhuma categoria selecionada",
				Alert.AlertType.WARNING);
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirme");
		alert.setHeaderText(null);
		alert.setContentText(String.format("Deseja remover a categoria '%s'?", selecionado.getNome()));
		alert.getButtonTypes().clear();
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = alert.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		try {
			repoCategoria.deletarPeloId(selecionado.getId());

			Alerts.showAlert("Successo", null, "Categoria removida com sucesso!",
				Alert.AlertType.INFORMATION);

			// Atualiza lista de exemplares
			tabelaCategorias.getItems().remove(tabelaCategorias.getSelectionModel().getSelectedIndex());
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao remover a categoria:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnAdicionar() {
		RepositorioCategoria repoCategoria = Main.getGerenciadorRepositorio().getRepositorio(RepositorioCategoria.class);

		TextInputDialog input = new TextInputDialog();

		input.setTitle("Adicionar categoria");
		input.setHeaderText(null);
		input.setContentText("Nome da categoria: ");

		Optional<String> resultado = input.showAndWait();
		if (!resultado.isPresent() || resultado.get().isEmpty()) {
			return;
		}

		String nome = resultado.get();

		if (repoCategoria.buscarPeloNome(nome) != null) {
			Alerts.showAlert("Erro", null, "Já existe uma categoria com este nome!",
				Alert.AlertType.ERROR);
			return;
		}

		Categoria cat = new Categoria(nome);

		try {
			repoCategoria.cadastrar(cat);

			Alerts.showAlert("Successo", null, "Categoria cadastrada com sucesso!",
				Alert.AlertType.INFORMATION);

			// Atualiza lista de exemplares
			tabelaCategorias.setItems(FXCollections.observableList(repoCategoria.buscarTodas()));
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao adicionar a categoria:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}
}
