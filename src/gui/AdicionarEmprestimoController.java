package gui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import application.Main;
import biblioteca.entidades.Emprestimo;
import biblioteca.entidades.ExemplarLivro;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.GerenciadorRepositorio;
import biblioteca.repositorio.RepositorioEmprestimo;
import biblioteca.repositorio.RepositorioExemplarLivro;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

public class AdicionarEmprestimoController {

	@FXML
	private TextField matriculaUsuario;
	
	@FXML
	private TextField codigoExemplar;
	
	public void onBtCadastrarEmprestimo() {
	  try {
	    cadastrar();
	  } catch (Exception ex) {
	    Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar o emprestimo:", ex.getMessage(), AlertType.ERROR);
      ex.printStackTrace();
	  }
	}
	
	public void cadastrar() {
	  GerenciadorRepositorio repos = Main.getGerenciadorRepositorio();
	  RepositorioUsuario repoUsuario = repos.getRepositorio(RepositorioUsuario.class);
	  RepositorioExemplarLivro repoExemplar = repos.getRepositorio(RepositorioExemplarLivro.class);
	  RepositorioEmprestimo repoEmprestimo = repos.getRepositorio(RepositorioEmprestimo.class);
	  
	  // TODO: validar matricula/codigoexemplar Long.parseLong
	  
	  Usuario usuario = repoUsuario.buscarPelaMatricula(Long.parseLong(matriculaUsuario.getText()));
	  
	  if (usuario == null) {
	    Alerts.showAlert("Erro", null, "Não existe um usuário com a matrícula: " + matriculaUsuario.getText(), 
	        AlertType.ERROR);
	    return;
	  }
	  
	  ExemplarLivro exemplar = repoExemplar.buscarPeloId(Integer.parseInt(codigoExemplar.getText()));
	  
	  if (exemplar == null) {
	    Alerts.showAlert("Erro", null, "Não existe um exemplar com o código: " + codigoExemplar.getText(), 
          AlertType.ERROR);
      return;
	  }
	  
	  if (!exemplar.estaDisponivel()) {
	    // TODO: informar a data que estará disponivel??
	    Alerts.showAlert("Erro", null, "O exemplar com o código " + codigoExemplar.getText() + " não está disponível!", 
          AlertType.ERROR);
	    return;
	  }
	  
	  Alert confirmaLivro = new Alert(AlertType.CONFIRMATION);
    confirmaLivro.setHeaderText("Você está prestes a cadastrar o seguinte emprestimo:");
    confirmaLivro.setTitle("Confirme os dados.");

    StringBuilder sb = new StringBuilder();
    sb.append("Usuário:").append("\n");
    sb.append("  Nome: " + usuario.getNome()).append('\n');
    sb.append("  Tipo: " + usuario.getTipo().getDescricao()).append('\n');
    sb.append('\n');
    sb.append("Titulo do livro: ").append(exemplar.getLivro().getTitulo()).append('\n');
    confirmaLivro.setContentText(sb.toString());
    
    Optional<ButtonType> respostaOpt = confirmaLivro.showAndWait();
    if (!respostaOpt.isPresent() || respostaOpt.get() != ButtonType.OK) {
      return;
    }
	  
	  Emprestimo emprestimo = new Emprestimo();
	  emprestimo.setUsuario(usuario);
	  emprestimo.setExemplar(exemplar);
	  emprestimo.setDataEmprestou(LocalDate.now());
	  emprestimo.setDataLimiteDevolucao(LocalDate.now().plusDays(usuario.getTipo().getQuantidadeDiasEmprestimo()));
	  
	  repoEmprestimo.cadastrar(emprestimo);
	  
	  exemplar.setDisponivel(false);
	  repoExemplar.atualizar(exemplar);
	  
	  Alerts.showAlert("Empréstimo cadastrado", null, "O usuário deve devolver o livro até o dia " + 
	      emprestimo.getDataLimiteDevolucao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), AlertType.CONFIRMATION);
	  
	  // TODO: limpar campos
	}
	
}
