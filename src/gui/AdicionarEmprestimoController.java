package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import application.Main;
import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Restricao;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.*;
import gui.util.Alerts;
import gui.util.Utils;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class AdicionarEmprestimoController {

  @FXML
  private TextField fieldMatricula;

  @FXML
  private TextField fieldCodExemplar;

  public void onBtCadastrarEmprestimo() {
    try {
      cadastrar();
    } catch (Exception ex) {
      Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar o emprestimo:", ex.getMessage(), AlertType.ERROR);
      ex.printStackTrace();
    }
  }

  private void cadastrar() {
    if (!validaCampos()) {
      return;
    }

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
    RepositorioUsuario repoUsuario = repos.getRepositorio(RepositorioUsuario.class);
    RepositorioExemplarLivro repoExemplar = repos.getRepositorio(RepositorioExemplarLivro.class);
    RepositorioEmprestimo repoEmprestimo = repos.getRepositorio(RepositorioEmprestimo.class);

    Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(fieldMatricula.getText()));

    if (usuario == null) {
      Alerts.showAlert("Erro", null, "Não existe um usuário com a matrícula: " + fieldMatricula.getText(),
        AlertType.ERROR);
      return;
    }

    // Checa restrição ativa
    RepositorioRestricao repoRestricao = repos.getRepositorio(RepositorioRestricao.class);
    Restricao restricao = repoRestricao.buscarRestricaoAtiva(usuario);
    if (restricao != null) {
      Alert confirma = new Alert(AlertType.WARNING);
      confirma.setTitle("Aviso");
      confirma.setHeaderText(String.format("O usuário '%s' possui uma restrição ativa!", usuario.getNome()));
      confirma.setContentText(
          "Motivo: " + restricao.getMotivo() + '\n' +
          "Data Início: " + df.format(restricao.getDataInicio()) + '\n' +
          "Data Fim: " + df.format(restricao.getDataFim()) + "\n\n" +
          "Deseja adicionar o empréstimo mesmo assim?\n\n"
      );
      confirma.getButtonTypes().clear();
      confirma.getButtonTypes().add(ButtonType.YES);
      confirma.getButtonTypes().add(ButtonType.NO);

      Optional<ButtonType> respostaOpt = confirma.showAndWait();
      if (!respostaOpt.isPresent() || respostaOpt.get() != ButtonType.YES) {
        return;
      }
    }

    ExemplarLivro exemplar = repoExemplar.buscarPeloId(Integer.parseInt(fieldCodExemplar.getText()));

    if (exemplar == null) {
      Alerts.showAlert("Erro", null, "Não existe um exemplar com o código: " + fieldCodExemplar.getText(),
        AlertType.ERROR);
      return;
    }

    if (!exemplar.estaDisponivel()) {
      // TODO: informar a data que estará disponivel??
      Alerts.showAlert("Erro", null, "O exemplar com o código " + fieldCodExemplar.getText() + " não está disponível!",
        AlertType.ERROR);
      return;
    }

    LocalDate dataDevolucao = LocalDate.now().plusDays(usuario.getTipo().getQuantidadeDiasEmprestimo());

    Alert confirmaLivro = new Alert(AlertType.CONFIRMATION);
    confirmaLivro.setHeaderText("Você está prestes a cadastrar o seguinte emprestimo:");
    confirmaLivro.setTitle("Confirme os dados.");

    String sb = "Usuário:" + "\n" +
      "  Nome: " + usuario.getNome() + '\n' +
      "  Tipo: " + usuario.getTipo().getDescricao() + '\n' +
      "Titulo do livro: " + exemplar.getLivro().getTitulo() + '\n' +
      "Data limite para devolução: " + dataDevolucao.format(df) + '\n';
    confirmaLivro.setContentText(sb);

    Optional<ButtonType> respostaOpt = confirmaLivro.showAndWait();
    if (!respostaOpt.isPresent() || respostaOpt.get() != ButtonType.OK) {
      return;
    }

    Emprestimo emprestimo = new Emprestimo();
    emprestimo.setUsuario(usuario);
    emprestimo.setExemplar(exemplar);
    emprestimo.setDataEmprestou(LocalDate.now());
    emprestimo.setDataLimiteDevolucao(dataDevolucao);

    repoEmprestimo.cadastrar(emprestimo);

    exemplar.setDisponivel(false);
    repoExemplar.atualizar(exemplar);

    Alerts.showAlert("Sucesso", null, "Emprestimo cadastrado!", AlertType.INFORMATION);

    Utils.limpaCamposDinamicamente(this);
  }

  private boolean validaCampos() {
    try {
      ValidadorCampo.valida(fieldMatricula, "Matrícula", Validadores.NAO_VAZIO, Validadores.NUMERO_LONG);
      ValidadorCampo.valida(fieldCodExemplar, "Código do Exemplar", Validadores.NAO_VAZIO, Validadores.NUMERO_INT);
      return true;
    } catch (RuntimeException ex) {
      Alerts.showAlert("Aviso", null, ex.getMessage(), AlertType.WARNING);
      return false;
    }
  }
}
