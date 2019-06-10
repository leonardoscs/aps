package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.relatorio.RelatorioEmprestimosUsuarioSQL;
import biblioteca.relatorio.RelatorioMaisEmprestadosSQL;
import biblioteca.repositorio.RepositorioAutor;
import biblioteca.repositorio.RepositorioCategoria;
import gui.util.Alerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class MainViewController implements Initializable{
	
	public void onMenuItemCadastrarUsuario() {
		loadView("/gui/CadastrarUsuarioView.fxml");
	}
	
	public void onMenuItemRemoverUsuario() {
		loadView("/gui/RemoverUsuarioView.fxml");
	}
	
	public void onMenuItemCadastrarLivro() {
		loadView("/gui/CadastrarLivroView.fxml");
	}
	
	public void onMenuItemRemoverLivro() {
		loadView("/gui/RemoverLivroView.fxml");
	}
	
	public void onMenuItemAdicionarEmprestimo() {
		loadView("/gui/AdicionarEmprestimoView.fxml");
	}
	
	public void onMenuItemAbaterEmprestimo() {
		loadView("/gui/AbaterEmprestimoView.fxml");
	}
	
	public void onMenuItemAdicionarRestricao() {
		loadView("/gui/AdicionarRestricaoView.fxml");
	}
	
	public void onMenuItemRemoverRestricao() {
		loadView("/gui/RemoverRestricaoView.fxml");
	}
	
	public void onMenuItemConsultaDeLivro() {
		loadView("/gui/ConsultarLivroView.fxml");
	}
	
	public void onMenuItemConsultaDeEmprestimo() {
		loadView("/gui/ConsultarEmprestimoView.fxml");
	}

	public void onMenuItemCadastrarExemplar() {
	  loadView("/gui/CadastrarExemplarView.fxml");
	}

	public void onMenuCadastrarTipoUsuario() {
		loadView("/gui/CadastrarTipoUsuarioView.fxml");
	}
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		
	}
	
	private synchronized void loadView(String caminhoDaView) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoDaView));
			VBox newVBox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox)((ScrollPane)mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			
			mainVBox.getChildren().clear();
			
			mainVBox.getChildren().add(mainMenu);
			
			mainVBox.getChildren().addAll(newVBox.getChildren());
		} catch (IOException e) {
			Alerts.showAlert("Algo deu errado meu consagrado", null, e.getMessage(), AlertType.ERROR);
			e.printStackTrace();
		}
	}

	public void onMenuItemRelatorioMaisEmprestados() {
		try {
			Path dirRelatorios = Paths.get("./relatorios/");
			if (Files.notExists(dirRelatorios)) {
				Files.createDirectory(dirRelatorios);
			}

			File file = new File("./relatorios/mais-emprestados-ultimo-ano.txt");

			// Não gosto do fato disso (abrindo conexão com o banco e tals) estar sendo
			// feito aqui, mas estamos sem tempo, então é o que tem pra hoje.
			Connection conn = DriverManager.getConnection(Main.urlBanco, Main.usuarioBanco, Main.senhaBanco);

			RelatorioMaisEmprestadosSQL re = new RelatorioMaisEmprestadosSQL(conn);
			re.gerarEmArquivo(file);
			Alerts.showAlert("Sucesso", null, "Relatório salvo no arquivo: " + file.getPath(),
				AlertType.INFORMATION);
		} catch (Exception e) {
			e.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao gerar o relatório:", e.getMessage(), AlertType.ERROR);
		}
	}

	public void onMenuItemRelatorioEmprestimosPorUsuario() {
		try {
			Path dirRelatorios = Paths.get("./relatorios/");
			if (Files.notExists(dirRelatorios)) {
				Files.createDirectory(dirRelatorios);
			}

			File file = new File("./relatorios/emprestimos-por-usuario.txt");

			Connection conn = DriverManager.getConnection(Main.urlBanco, Main.usuarioBanco, Main.senhaBanco);

			RelatorioEmprestimosUsuarioSQL re = new RelatorioEmprestimosUsuarioSQL(conn);
			re.gerarEmArquivo(file);
			Alerts.showAlert("Sucesso", null, "Relatório salvo no arquivo: " + file.getPath(),
				AlertType.INFORMATION);
		} catch (Exception e) {
			e.printStackTrace();
			Alerts.showAlert("Erro", "Ocorreu um erro ao gerar o relatório:", e.getMessage(), AlertType.ERROR);
		}
	}

  public void onMenuItemCadastrarAutor() {
		loadView("/gui/CadastrarAutorView.fxml");
	}

	public void onMenuItemCadastrarCategoria() {
		loadView("/gui/CadastrarCategoriaView.fxml");
	}

	public void onMenuItemCadastrarEditora() {
		loadView("/gui/CadastrarEditoraView.fxml");
	}
}
