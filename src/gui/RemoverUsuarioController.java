package gui;

import application.Main;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.util.Optional;

public class RemoverUsuarioController {

	@FXML
	private TextField fieldMatricula;

	@FXML
	private void onBtRemover() {
		if (!validaCampos()) {
			return;
		}

		RepositorioUsuario repoUsuario = Main.getGerenciadorRepositorio().getRepositorio(RepositorioUsuario.class);

		Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(fieldMatricula.getText()));

		if (usuario == null) {
			Alerts.showAlert("Aviso", null, "Não existe um usuário com a matrícula: " + fieldMatricula.getText(),
				AlertType.INFORMATION);
			return;
		}

		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Confirme");
		alert.setHeaderText(null);
		alert.setContentText(String.format("Deseja remover o usuário \"%s\" de matrícula \"%d\"?", usuario.getNome(), usuario.getMatricula()));
		alert.getButtonTypes().clear();
		alert.getButtonTypes().add(ButtonType.YES);
		alert.getButtonTypes().add(ButtonType.NO);

		ButtonType resposta = alert.showAndWait().orElse(null);
		if (resposta != ButtonType.YES) {
			return;
		}

		repoUsuario.deletarPeloId(usuario.getId());
		Alerts.showAlert("Sucesso", null, "Usuário removido com sucesso", AlertType.INFORMATION);

		fieldMatricula.setText("");
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
	
}
