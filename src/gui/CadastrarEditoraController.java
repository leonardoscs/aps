package gui;


import application.Main;
import biblioteca.entidades.Editora;
import biblioteca.repositorio.RepositorioEditora;
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

public class CadastrarEditoraController implements Initializable {

	@FXML
	private TextField inputNomeFiltro;

	@FXML
	private TableView<Editora> tabelaEditoras;
  
	@FXML
  private TableColumn<Editora, String> nomeCol;

	private List<Editora> editorasCarregadas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeCol.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getNome()));

		atualizarTabela();
	}

	public void onBtnRemoverSelecionado() {
		RepositorioEditora repoEditora = Main.getGerenciadorRepositorio().getRepositorio(RepositorioEditora.class);

		Editora selecionado = tabelaEditoras.getSelectionModel().getSelectedItem();

		if (selecionado == null) {
			Alerts.showAlert("Aviso", null, "Nenhuma editora selecionada",
				Alert.AlertType.WARNING);
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirme");
		alert.setHeaderText(String.format("Deseja remover a editora '%s'?", selecionado.getNome()));
		alert.setContentText("Todos os livros desta editora TAMBÉM serão removidos, assim como todas informações relacionadas a tais livros.\nDeseja remover mesmo assim?\n");
		alert.getButtonTypes().clear();
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = alert.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		try {
			repoEditora.deletarPeloId(selecionado.getId());

			Alerts.showAlert("Successo", null, "Editora removida com sucesso!",
				Alert.AlertType.INFORMATION);

			tabelaEditoras.getItems().remove(tabelaEditoras.getSelectionModel().getSelectedIndex());
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao remover a editora:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnAdicionar() {
		RepositorioEditora repoEditora = Main.getGerenciadorRepositorio().getRepositorio(RepositorioEditora.class);

		TextInputDialog input = new TextInputDialog();

		input.setTitle("Adicionar editora");
		input.setHeaderText(null);
		input.setContentText("Nome da editora: ");

		Optional<String> resultado = input.showAndWait();
		if (!resultado.isPresent() || resultado.get().isEmpty()) {
			return;
		}

		String nome = resultado.get();

		if (repoEditora.buscarPeloNome(nome) != null) {
			Alerts.showAlert("Erro", null, "Já existe uma editora com esse nome!",
				Alert.AlertType.ERROR);
			return;
		}

		Editora autor = new Editora(nome);

		try {
			repoEditora.cadastrar(autor);

			Alerts.showAlert("Successo", null, "Editora cadastrada com sucesso!",
				Alert.AlertType.INFORMATION);

			tabelaEditoras.setItems(FXCollections.observableList(repoEditora.buscarTodos()));
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar a editora:", ex.getMessage(), Alert.AlertType.ERROR);
		}
	}

	public void onBtnFiltrar() {
		if (inputNomeFiltro.getText().isEmpty()) {
			tabelaEditoras.setItems(FXCollections.observableList(editorasCarregadas));
			return;
		}

		String nome = inputNomeFiltro.getText().toLowerCase();
		String[] partes = nome.split(" ");

		Predicate<Editora> filtro = partes.length == 1
			? a -> a.getNome().toLowerCase().contains(partes[0])
			: a -> Arrays.stream(partes).anyMatch(parte -> a.getNome().toLowerCase().contains(parte));

		List<Editora> filtrado = editorasCarregadas.stream()
			.filter(filtro)
			.collect(Collectors.toList());

		tabelaEditoras.setItems(FXCollections.observableList(filtrado));
	}

	private void atualizarTabela() {
		RepositorioEditora repoEditora = Main.getGerenciadorRepositorio().getRepositorio(RepositorioEditora.class);

		editorasCarregadas = repoEditora.buscarTodos();

		tabelaEditoras.setItems(FXCollections.observableList(editorasCarregadas));
	}
}
