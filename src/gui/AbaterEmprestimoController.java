package gui;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import application.Main;
import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioExemplarLivro;
import gui.util.Alerts;
import gui.util.Utils;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class AbaterEmprestimoController {

  @FXML
  private TextField fieldCodExemplar;

  public void onBtAbaterEmprestimo() {
    try {
      abaterEmprestimo();
    } catch (Exception ex) {
      Alerts.showAlert("Erro", "Ocorreu um erro ao abater o emprestimo:", ex.getMessage(), AlertType.ERROR);
      ex.printStackTrace();
    }
  }
  
  private void abaterEmprestimo() {
    if (!validaCampos()) {
      return;
    }

    GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
    RepositorioExemplarLivro repoExemplar = repos.getRepositorio(RepositorioExemplarLivro.class);
    RepositorioEmprestimo repoEmprestimo = repos.getRepositorio(RepositorioEmprestimo.class);

    ExemplarLivro exemplar = repoExemplar.buscarPeloId(Integer.parseInt(fieldCodExemplar.getText()));
    
    if (exemplar == null) {
      Alerts.showAlert("Erro", null, "Não existe um exemplar com o código: " + fieldCodExemplar.getText(),
          AlertType.ERROR);
      return;
    }
    
    Emprestimo emprestimo = repoEmprestimo.buscarEmprestimoAtivoPorExemplar(exemplar);
    
    if (emprestimo == null) {
      Alerts.showAlert("Erro", null, "Não existe um emprestimo ativo para este exemplar.", 
          AlertType.ERROR);
      return;
    }

    DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    Alert confirmaLivro = new Alert(AlertType.CONFIRMATION);
    confirmaLivro.setHeaderText("Abater o seguinte emprestimo?");
    confirmaLivro.setTitle("Confirme");

    StringBuilder sb = new StringBuilder();
    sb.append("Titulo do livro: ").append(exemplar.getLivro().getTitulo()).append('\n');
    sb.append("Data que foi emprestado: ").append(emprestimo.getDataEmprestou().format(df)).append('\n');
    sb.append("Data limite para devolução: ").append(emprestimo.getDataLimiteDevolucao().format(df)).append('\n');
    sb.append("Nome do Usuário: ").append(emprestimo.getUsuario().getNome()).append('\n');
    confirmaLivro.setContentText(sb.toString());
    
    // TODO: adicionar restricao caso tenha devolvido atrasado

    ButtonType resposta = confirmaLivro.showAndWait().orElse(null);
    if (resposta != ButtonType.OK) {
      return;
    }
    
    repoEmprestimo.marcarEmprestimoComoDevolvido(emprestimo);
    
    exemplar.setDisponivel(true);
    repoExemplar.atualizar(exemplar);
    
    Alerts.showAlert("Sucesso", null, "Emprestimo abatido.", AlertType.INFORMATION);

    Utils.limpaCamposDinamicamente(this);
  }

  private boolean validaCampos() {
    try {
      ValidadorCampo.valida(fieldCodExemplar, "Código do Exemplar", Validadores.NAO_VAZIO, Validadores.NUMERO_INT);
      return true;
    } catch (RuntimeException ex) {
      Alerts.showAlert("Aviso", null, ex.getMessage(), AlertType.WARNING);
      return false;
    }
  }
}
