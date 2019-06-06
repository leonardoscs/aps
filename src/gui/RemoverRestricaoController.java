package gui;

import application.Main;
import biblioteca.entidades.Restricao;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioRestricao;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.time.format.DateTimeFormatter;

public class RemoverRestricaoController {

	@FXML
	private TextField fieldMatricula;
	
	public void onBtRemoverRestricao() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repo = Main.getGerenciadorRepositorio();
		RepositorioUsuario repoUsuario = repo.getRepositorio(RepositorioUsuario.class);
		RepositorioRestricao repoRestricao = repo.getRepositorio(RepositorioRestricao.class);

		Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(fieldMatricula.getText()));

		if (usuario == null) {
			Alerts.showAlert("Erro", null, "Não existe um usuário com a matrícula: " + fieldMatricula.getText(),
				AlertType.ERROR);
			return;
		}

		Restricao restricaoAtiva = repoRestricao.buscarRestricaoAtiva(usuario);

		if (restricaoAtiva == null) {
			Alerts.showAlert("Erro", null, "Não existe uma restrição ativa para o usuário: " + usuario.getNome(),
				AlertType.ERROR);
			return;
		}

		DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		Alert confirma = new Alert(AlertType.CONFIRMATION);
		confirma.setTitle("Confirmação");
		confirma.setHeaderText(String.format("Remover a seguinte restrição do usuário '%s'?", usuario.getNome()));
		confirma.setContentText(
				"Motivo: " + restricaoAtiva.getMotivo() + '\n' +
				"Data Início: " + df.format(restricaoAtiva.getDataInicio()) + '\n' +
				"Data Fim: " + df.format(restricaoAtiva.getDataFim()) + "\n\n"
		);
		confirma.getButtonTypes().clear();
		confirma.getButtonTypes().add(ButtonType.YES);
		confirma.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = confirma.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		repoRestricao.deletarPeloId(restricaoAtiva.getId());

		Alerts.showAlert("Aviso", null, "Restrição removida com sucesso", AlertType.CONFIRMATION);
	}

	private boolean validaCampos() {
		try {
			ValidadorCampo.valida(fieldMatricula, "Matrícula", Validadores.NAO_VAZIO, Validadores.NUMERO_LONG);
			return true;
		} catch (RuntimeException ex) {
			Alerts.showAlert("Aviso", null, ex.getMessage(), AlertType.WARNING);
			return false;
		}
	}
}
