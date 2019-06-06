package gui;

import application.Main;
import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioLivro;
import gui.util.Alerts;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class RemoverLivroController {

	@FXML
	private TextField fieldCodLivro;
	
	public void onBtRemover() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
		RepositorioLivro repoLivro = repos.getRepositorio(RepositorioLivro.class);

		Livro livro = repoLivro.buscarPeloId(Integer.parseInt(fieldCodLivro.getText()));

		if (livro == null) {
			Alerts.showAlert("Erro", null, "Não existe um livro com o código: " + fieldCodLivro.getText(),
				Alert.AlertType.ERROR);
			return;
		}

		Alert confirma = new Alert(AlertType.WARNING);
		confirma.setTitle("Confirme");
		confirma.setHeaderText("Você está prestes a remover o seguinte livro:");

		String sb = "Titulo: " + livro.getTitulo() + '\n' +
			"Localização: " + livro.getLocalizacao() + '\n' +
			"Data de Publicação: " + livro.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")) + '\n' +
			"Quantidade de Páginas: " + livro.getQuantidadePaginas() + '\n' +
			"Descrição: " + '"' + livro.getDescricao() + '"' + '\n' +
			"Editora: " + livro.getEditora().getNome() + '\n' +
			"Autores: " + livro.getAutores().stream().map(Autor::getNome).collect(Collectors.joining(", ")) + '\n' +
			"Categorias: " + livro.getCategorias().stream().map(Categoria::getNome).collect(Collectors.joining(", ")) + '\n' + '\n' +
			"Ao remover um livro, todos os exemplares e emprestimos também serão removidos. Deseja continuar?\n\n";

		confirma.setContentText(sb);
		confirma.getButtonTypes().clear();
		confirma.getButtonTypes().add(ButtonType.YES);
		confirma.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = confirma.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		try {
			repoLivro.deletarPeloId(livro.getId());

			Alerts.showAlert("Successo", null, "Livro removido com sucesso!",
				Alert.AlertType.INFORMATION);

			fieldCodLivro.setText("");
		} catch(Exception ex) {
			ex.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao remover o livro!:", ex.getMessage(), Alert.AlertType.ERROR);
		}
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
