package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable{

	@FXML
	private MenuItem menuItemCadastarUsuario;
	
	@FXML
	private MenuItem menuItemRemoverUsuario;
	
	@FXML
	private MenuItem menuItemCadastrarLivro;
	
	@FXML
	private MenuItem menuItemRemoverLivro;
	
	@FXML
	private MenuItem menuItemAdicionarEmprestimo;
	
	@FXML
	private MenuItem menuItemAbaterEmprestimo;
	
	@FXML
	private MenuItem menuItemAdicionarRestricao;
	
	@FXML
	private MenuItem menuItemRemoverRestricao;
	
	@FXML
	private MenuItem menuItemConsultaDeLivros;

	@FXML
	private MenuItem menuItemConsultaDeEmprestimo;
	
	@FXML
	public void onMenuItemCadastrarUsuario() {
		System.out.println("a");
	}
	
	@FXML
	public void onMenuItemRemoverUsuario() {
		System.out.println("a1");
	}
	
	@FXML
	public void onMenuItemCadastrarLivro() {
		System.out.println("b");
	}
	
	@FXML
	public void onMenuItemRemoverLivro() {
		System.out.println("b1");
	}
	
	@FXML
	public void onMenuItemAdicionarEmprestimo() {
		System.out.println("c");
	}
	
	@FXML
	public void onMenuItemAbaterEmprestimo() {
		System.out.println("d");
	}
	
	@FXML
	public void onMenuItemAdicionarRestricao() {
		System.out.println("e");
	}
	
	@FXML
	public void onMenuItemRemoverRestricao() {
		System.out.println("f");
	}
	
	@FXML
	public void onMenuItemConsultaDeLivro() {
		System.out.println("g");
	}
	
	@FXML
	public void onMenuItemConsultaDeEmprestimo() {
		System.out.println("h");
	}
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
}
