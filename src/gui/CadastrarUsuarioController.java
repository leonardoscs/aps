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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
	
	@FXML
	private Label semNome;
	
	@FXML
	private Label semTipo;
	
	@FXML
	private Label semEmail;
	
	@FXML
	private Label semMatricula;
	
	@FXML
	private Label semTelefone;
	
	private boolean validaCampos() {
	  boolean valido = true;

	  // Limpa texto das labels...
	  semNome.setText("");
    semTipo.setText("");
    semEmail.setText("");
    semMatricula.setText("");
    semTelefone.setText("");
    
	  if (matricula.getText().isEmpty()) {
      semMatricula.setText("Campo matricula não pode estar vazio");
      valido = false;
    }
    if (tipo.getSelectionModel().getSelectedItem() == null) {
      semTipo.setText("Campo tipo não pode estar vazio.");
      valido = false;
    } 
    if (telefone.getText().isEmpty()) {
      semTelefone.setText("Campo telefone não pode estar vazio");
      valido = false;
    }
    if (nome.getText().isEmpty()) {
      semNome.setText("Campo nome não pode estar vazio");
      valido = false;
    } 
    if (email.getText().isEmpty()) {
      semEmail.setText("Campo email não pode estar vazio");
      valido = false;
    }

    try {
      Long.parseLong(matricula.getText());
    } catch (NumberFormatException x) {
      semMatricula.setText("Campo matricula não é um número válido.");
      valido = false;
    }
    
    return valido;
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
