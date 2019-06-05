package gui;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import application.Main;
import biblioteca.entidades.TipoUsuario;
import biblioteca.entidades.Usuario;
import biblioteca.repositorio.RepositorioTipoUsuario;
import biblioteca.repositorio.RepositorioUsuario;
import gui.util.Alerts;
import gui.util.Utils;
import gui.validador.ValidadorCampo;
import gui.validador.Validadores;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

public class CadastrarUsuarioController implements Initializable{
	
	@FXML
	private TextField nome;
	
	@FXML
	private TextField matricula;
	
	@FXML
	private TextField telefone;
	
	@FXML
	private TextField email;
	
	@FXML
	private ChoiceBox<TipoUsuario> tipo;

  private boolean validaCampos() {
    try {
      ValidadorCampo.valida(nome, "Nome", Validadores.NAO_VAZIO);
      ValidadorCampo.valida(telefone, "Telefone", Validadores.NAO_VAZIO);
      ValidadorCampo.valida(matricula, "Matrícula", Validadores.NAO_VAZIO, Validadores.NUMERO_LONG);
      ValidadorCampo.valida(tipo, "Tipo Usuário", Validadores.NAO_VAZIO);
      ValidadorCampo.valida(email, "Email", Validadores.NAO_VAZIO);
      return true;
    } catch (RuntimeException ex) {
      Alerts.showAlert("Aviso", null, ex.getMessage(), Alert.AlertType.WARNING);
      return false;
    }
  }
	
	// TODO: criar view para cadastrar/remover Tipos de Usuario
	
	public void onBtCadastrar() {
	  // Só procede (cadastra) caso todos os campos sejam validos.
	  if (!validaCampos()) {
	    return;
    }

	  // TODO: verificar a existencia de usuarios com a mesma matricula/email...
	  // se existem, mostrar alerta e retornar.
	  
	  try {
      RepositorioUsuario repoUsuario = Main.getGerenciadorRepositorio().getRepositorio(RepositorioUsuario.class);
      
      Usuario usuario = new Usuario();
      usuario.setNome(nome.getText());
      usuario.setEmail(email.getText());
      usuario.setMatricula(Long.parseLong(matricula.getText()));
      usuario.setTelefone(telefone.getText());
      usuario.setTipo(tipo.getSelectionModel().getSelectedItem());
      
      repoUsuario.cadastrar(usuario);
      
      Alerts.showAlert("Aviso", null, "Usuário cadastrado com sucesso", AlertType.INFORMATION);

      Utils.limpaCamposDinamicamente(this);
    } catch (Exception ex) {
      Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar o usuário", ex.getMessage(), AlertType.ERROR);
    }
	}
	
	@Override
	public void initialize(URL url, ResourceBundle arb) {
    try {
      RepositorioTipoUsuario repoTipo = Main.getGerenciadorRepositorio().getRepositorio(RepositorioTipoUsuario.class);
      
      List<TipoUsuario> tipos = repoTipo.buscarTodos();
      tipo.getItems().addAll(tipos);
     
      tipo.setConverter(new StringConverter<TipoUsuario>() {
        private Map<String, TipoUsuario> usuariosPorDescricao = 
            tipos.stream().collect(Collectors.toMap(TipoUsuario::getDescricao, Function.identity()));
        
        @Override
        public String toString(TipoUsuario tipo) {
          return tipo.getDescricao();
        }
        
        @Override
        public TipoUsuario fromString(String tipo) {
          return usuariosPorDescricao.get(tipo);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
      Alerts.showAlert("Erro", "Não foi possível carregar os tipos de usuários.", e.getMessage(), AlertType.ERROR);
    }
	}
}
