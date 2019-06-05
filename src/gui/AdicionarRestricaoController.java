package gui;

import application.Main;
import biblioteca.entidades.Restricao;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioRestricao;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import gui.util.Utils;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.Optional;

public class AdicionarRestricaoController {

	@FXML
	private TextField fieldMatricula;
	
	@FXML
	private TextField fieldDataFim;
	
	@FXML
	private TextField fieldMotivo;
	
	public void onBtAdicionarRestricao() {
		if (!validaCampos()) {
			return;
		}

		GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
		RepositorioUsuario repoUsuario = repos.getRepositorio(RepositorioUsuario.class);
		RepositorioRestricao repoRestricao = repos.getRepositorio(RepositorioRestricao.class);

		Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(fieldMatricula.getText()));

		if (usuario == null) {
			Alerts.showAlert("Erro", null, "Não existe um usuário com a matrícula: " + fieldMatricula.getText(),
				AlertType.ERROR);
			return;
		}

		Alert confirma = new Alert(AlertType.CONFIRMATION);
		confirma.setHeaderText("Você está prestes a cadastrar a seguinte restrição:");
		confirma.setTitle("Confirme os dados.");

		String sb =
			"Restrição:" + "\n" +
				"  Motivo: " + fieldMotivo.getText() + '\n' +
				"  Data Término: " + fieldDataFim.getText() + '\n' +
				"Usuário:" + "\n" +
				"  Nome: " + usuario.getNome() + '\n' +
				"  Matrícula: " + usuario.getMatricula() + '\n';
		confirma.setContentText(sb);

		Optional<ButtonType> respostaOpt = confirma.showAndWait();
		if (!respostaOpt.isPresent() || respostaOpt.get() != ButtonType.OK) {
			return;
		}

		Restricao restricao = new Restricao();
		restricao.setMotivo(fieldMotivo.getText());
		restricao.setDataInicio(LocalDate.now());
		restricao.setDataFim(converterData(fieldDataFim.getText()));
		restricao.setUsuario(usuario);

		try {
			repoRestricao.cadastrar(restricao);
			Alerts.showAlert("Aviso", null, "Restrição adicionada!", AlertType.CONFIRMATION);
			Utils.limpaCamposDinamicamente(this);
		} catch (Exception ex) {
			Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar a restrição:", ex.getMessage(), AlertType.ERROR);
			ex.printStackTrace();
		}
	}

	private boolean validaCampos() {
		try {
			ValidadorCampo.valida(fieldMatricula, "Matrícula", Validadores.NAO_VAZIO, Validadores.NUMERO_LONG);
			ValidadorCampo.valida(fieldMotivo, "Motivo", Validadores.NAO_VAZIO);
			ValidadorCampo.valida(fieldDataFim, "Data Fim", Validadores.DATA);
			return true;
		} catch (RuntimeException ex) {
			Alerts.showAlert("Aviso", null, ex.getMessage(), AlertType.WARNING);
			return false;
		}
	}

	private LocalDate converterData(String text) {
		String[] partes = text.split("/");
		return LocalDate.of(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
	}

}
