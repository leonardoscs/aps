package gui;


import application.Main;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Livro;
import biblioteca.entidades.TipoUsuario;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioLivro;
import biblioteca.repositorio.RepositorioTipoUsuario;
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

public class CadastrarTipoUsuarioController implements Initializable {

  @FXML
  private TableView<TipoUsuario> tabelaTipoUsuarios;

  @FXML
  private TableColumn<TipoUsuario, String> colDescricao;

  @FXML
  private TableColumn<TipoUsuario, String> colQtdDias;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    colDescricao.setCellValueFactory(f -> new ReadOnlyStringWrapper(f.getValue().getDescricao()));
    colQtdDias.setCellValueFactory(f -> new ReadOnlyStringWrapper(Integer.toString(f.getValue().getQuantidadeDiasEmprestimo())));

    RepositorioTipoUsuario repoTipo = Main.getGerenciadorRepositorio().getRepositorio(RepositorioTipoUsuario.class);
    tabelaTipoUsuarios.setItems(FXCollections.observableList(repoTipo.buscarTodos()));
  }

  public void onBtnRemoverSelecionado() {
    RepositorioTipoUsuario repoTipo = Main.getGerenciadorRepositorio().getRepositorio(RepositorioTipoUsuario.class);

    TipoUsuario tipoSelecionado = tabelaTipoUsuarios.getSelectionModel().getSelectedItem();

    if (tipoSelecionado == null) {
      Alerts.showAlert("Aviso", null, "Nenhum tipo selecionado",
        Alert.AlertType.WARNING);
      return;
    }

    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirme");
    alert.setHeaderText(null);
    alert.setContentText(String.format("Deseja remover o Tipo de Usuário '%s'?", tipoSelecionado.getDescricao()));
    alert.getButtonTypes().clear();
    alert.getButtonTypes().add(ButtonType.YES);
    alert.getButtonTypes().add(ButtonType.NO);

    Optional<ButtonType> respostaOpt = alert.showAndWait();
    if (!respostaOpt.isPresent() || respostaOpt.get() == ButtonType.NO) {
      return;
    }

    repoTipo.deletarPeloId(tipoSelecionado.getId());

    Alerts.showAlert("Successo", null, "Tipo de Usuário removido com sucesso!",
      Alert.AlertType.INFORMATION);

    // Atualiza
    tabelaTipoUsuarios.setItems(FXCollections.observableList(repoTipo.buscarTodos()));
  }

  public void onBtnAdicionar() {
    TextInputDialog descricaoInput = new TextInputDialog();

    descricaoInput.setTitle("Cadastrar um novo Tipo de Usuário");
    descricaoInput.setHeaderText(null);
    descricaoInput.setContentText("Descrição do Tipo: ");

    Optional<String> resultadoDescricao = descricaoInput.showAndWait();

    if (!resultadoDescricao.isPresent() || resultadoDescricao.get().isEmpty()) {
      return;
    }

    String descricao = resultadoDescricao.get();

    RepositorioTipoUsuario repoTipo = Main.getGerenciadorRepositorio().getRepositorio(RepositorioTipoUsuario.class);

    if (repoTipo.buscarPelaDescricao(descricao) != null) {
      Alerts.showAlert("Erro", null, "Já existe um Tipo de Usuário com a descrição: " + descricao,
        Alert.AlertType.ERROR);
      return;
    }

    TextInputDialog quantidadeDiasInput = new TextInputDialog();

    quantidadeDiasInput.setTitle("Cadastrar um novo Tipo de Usuário");
    quantidadeDiasInput.setHeaderText(null);
    quantidadeDiasInput.setContentText("Duração máxima em dias do emprestimo: ");

    Optional<String> resultadoQtdDias = quantidadeDiasInput.showAndWait();

    if (!resultadoQtdDias.isPresent() || resultadoQtdDias.get().isEmpty()) {
      return;
    }

    int quantidadeDias;
    try {
      quantidadeDias = Integer.parseInt(resultadoQtdDias.get());
    } catch (NumberFormatException ex) {
      Alerts.showAlert("Erro", null,
        String.format("'%s' não é um número válido!", resultadoQtdDias.get()), Alert.AlertType.ERROR);
      return;
    }

    TipoUsuario tipo = new TipoUsuario();
    tipo.setDescricao(descricao);
    tipo.setQuantidadeDiasEmprestimo(quantidadeDias);
    repoTipo.cadastrar(tipo);

    Alerts.showAlert("Sucesso", null, "Tipo de Usuário cadastrado com sucesso!", Alert.AlertType.CONFIRMATION);

    // Atualizar
    tabelaTipoUsuarios.setItems(FXCollections.observableList(repoTipo.buscarTodos()));
  }

}
