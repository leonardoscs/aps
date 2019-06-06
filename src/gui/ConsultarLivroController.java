package gui;


import application.Main;
import biblioteca.entidades.*;
import biblioteca.repositorio.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ConsultarLivroController implements Initializable {

	@FXML
	private TextField fieldTitulo;

	@FXML
	private TextField fieldAutor;
	
	@FXML
	private TableView<Livro> tabelaLivros;
	
	@FXML
  private TableColumn<Livro, String> colTitulo;
  
	@FXML
  private TableColumn<Livro, String> colLocalizacao;
	
	@FXML
  private TableColumn<Livro, String> colEditora;

	@FXML
	private TableColumn<Livro, String> colAutores;

	@FXML
	private TableColumn<Livro, String> colCategorias;

	public void onBtnBuscar() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
		RepositorioLivro repoLivro = repos.getRepositorio(RepositorioLivro.class);
		RepositorioAutor repoAutor = repos.getRepositorio(RepositorioAutor.class);

		List<Livro> resultado;
		if (!fieldTitulo.getText().isEmpty()) {
			resultado = repoLivro.buscarPeloTituloParcial(fieldTitulo.getText());
		} else {
			List<Autor> autores = repoAutor.buscarPeloNomeParcial(fieldAutor.getText());
			resultado = autores.stream()
				.map(repoLivro::buscarPeloAutor)
				.flatMap(List::stream)
				.collect(Collectors.toList());
		}
		if (resultado.size() == 0) {
			Alerts.showAlert("Aviso", null, "Nenhum livro encontrado!", Alert.AlertType.WARNING);
			return;
		}
		tabelaLivros.setItems(FXCollections.observableList(resultado));
	}

	private boolean validaCampos() {
		if (fieldTitulo.getText().isEmpty() && fieldAutor.getText().isEmpty()) {
			Alerts.showAlert("Aviso", null, "Pelo menos um dos campos deve ser preenchido!",
				Alert.AlertType.WARNING);
			return false;
		}
		return true;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		colTitulo.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getTitulo()));
		colLocalizacao.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getLocalizacao()));
		colAutores.setCellValueFactory(f -> new ReadOnlyStringWrapper(
			f.getValue().getAutores().stream().map(Autor::getNome).collect(Collectors.joining(", "))
		));
		colEditora.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getEditora().getNome()));
		colCategorias.setCellValueFactory(f -> new ReadOnlyStringWrapper(
			f.getValue().getCategorias().stream().map(Categoria::getNome).collect(Collectors.joining(", "))
		));
	}
}
