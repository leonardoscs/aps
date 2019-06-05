package gui;

import application.Main;
import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.entidades.Editora;
import biblioteca.entidades.Livro;
import biblioteca.repositorio.RepositorioAutor;
import biblioteca.repositorio.RepositorioEditora;
import biblioteca.repositorio.RepositorioLivro;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CadastrarLivroController {
	
  // TODO: categorias...
  
	@FXML
	private TextField fieldTitulo;
	
	@FXML
	private TextArea fieldDescricao;
	 
	@FXML
	private TextField fieldQtdPaginas;
	
	@FXML
	private TextField fieldDataPublicacao;
	
	@FXML
	private TextField fieldLocalizacao;
	
	@FXML
	private TextField fieldEditora;
	
	@FXML
	private TextField fieldCategoria;

	// TODO:
	// Por enquanto isso será um TextField. Para adicionar mais de um autor
	// será necessário separar os nomes por virgula...
	// Se der tempo, dá pra mudar pra um botão de adicionar/selecionar ou algo assim...
	@FXML
	private TextField fieldAutores;
	
	private boolean validaCampos() {
	  Control[] campos = {
	    fieldTitulo, fieldDescricao, fieldDataPublicacao, fieldLocalizacao,
      fieldQtdPaginas, fieldEditora, fieldAutores
    };

	  for (Control campo : campos) {
	    String valor = (campo instanceof TextArea)
        ? ((TextArea) campo).getText()
        : ((TextField) campo).getText();

	    if (valor.isEmpty()) {
        Alerts.showAlert("Aviso", null, "Todos os campos precisam ser preenchidos", AlertType.CONFIRMATION);
        return false;
      }
    }

    // TODO: validar qtd paginas -> inteiro
    // TODO: valida data publicacao -> dia/mes/ano

    return true;
	}
	
	private List<Autor> buscaOuCriaAutores(String nomes) {
	  RepositorioAutor repoAutor = Main.getGerenciadorRepositorio().getRepositorio(RepositorioAutor.class);
	  
	  List<Autor> autores = new ArrayList<>();
	  String[] nomeAutores = nomes.split(",");
  
    for (String nomeAutor : nomeAutores) {
      nomeAutor = nomeAutor.trim(); // remove os espaços
      
      // Busca pelo nome do autor. Caso não exista, mostra um dialog
      // para cadastrar um novo autor.
      Autor autor = repoAutor.buscarPeloNome(nomeAutor);
      if (autor == null) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(String.format("Não existe um autor chamado '%s'.\nDeseja cadastrar um novo autor com esse nome?", nomeAutor));
        alert.getButtonTypes().clear();
        alert.getButtonTypes().add(ButtonType.YES);
        alert.getButtonTypes().add(ButtonType.NO);
        
        Optional<ButtonType> respostaOpt = alert.showAndWait();

        // Se não respostar ou responder NÃO, o livro não será cadastrado.
        if (!respostaOpt.isPresent() || respostaOpt.get() == ButtonType.NO) {
          continue;
        }
        
        autor = new Autor(nomeAutor);
        repoAutor.cadastrar(autor);
      }
      autores.add(autor);
    }
    
    return autores;
	}
	
	public void onBtCadastrar() {
	  if (!validaCampos()) {
	    return;
	  }
	  
	  
	  List<Autor> autores = buscaOuCriaAutores(fieldAutores.getText());
    Editora editora = buscaOuCriaEditora(fieldEditora.getText());
    
    // Se a editora é nula, significa que o usuário não a cadastrou
    // portanto não será possível cadastrar o livro.
    if (editora == null) {
      return;
    }
	  
    Livro livro = new Livro();
    livro.setEditora(editora);
    livro.setAutores(autores);
    livro.setTitulo(fieldTitulo.getText());
    livro.setDescricao(fieldDescricao.getText());
    livro.setQuantidadePaginas(Integer.parseInt(fieldQtdPaginas.getText()));
    livro.setCategorias(Collections.emptyList()); // TODO
    livro.setLocalizacao(fieldLocalizacao.getText());
    livro.setDataPublicacao(converterData(fieldDataPublicacao.getText()));
    
    Alert confirmaLivro = new Alert(AlertType.CONFIRMATION);
    confirmaLivro.setHeaderText("Você está prestes a cadastrar o seguinte livro:");
    confirmaLivro.setTitle("Confirme os dados.");

    StringBuilder sb = new StringBuilder();
    sb.append("Titulo: ").append(livro.getTitulo()).append('\n');
    sb.append("Localização: ").append(livro.getLocalizacao()).append('\n');
    sb.append("Data de Publicação: ").append(livro.getDataPublicacao().format(DateTimeFormatter.ofPattern("dd/MM/YYYY"))).append('\n');
    sb.append("Quantidade de Páginas: ").append(livro.getQuantidadePaginas()).append('\n');
    sb.append("Descrição: ").append('"').append(livro.getDescricao()).append('"').append('\n');
    sb.append("Editora: ").append(livro.getEditora().getNome()).append('\n');
    sb.append("Autores: ").append(livro.getAutores().stream().map(Autor::getNome).collect(Collectors.joining(","))).append('\n');
    sb.append("Categorias: ").append(livro.getCategorias().stream().map(Categoria::getNome).collect(Collectors.joining(","))).append('\n');
    confirmaLivro.setContentText(sb.toString());
    
    Optional<ButtonType> respostaOpt = confirmaLivro.showAndWait();
    if (!respostaOpt.isPresent() || respostaOpt.get() != ButtonType.OK) {
      return;
    }
    
    RepositorioLivro repoLivro = Main.getGerenciadorRepositorio().getRepositorio(RepositorioLivro.class);
    
    try {
      repoLivro.cadastrar(livro);
    } catch (Exception ex) {
      Alerts.showAlert("Erro", "Ocorreu um erro ao cadastrar o livro:", ex.getMessage(), AlertType.ERROR);
      ex.printStackTrace();
      return;
    }
    
    // TODO: limpar todos os campos
  
		Alerts.showAlert("Aviso", null, "Livro cadastrado", AlertType.INFORMATION);
	}

	private LocalDate converterData(String text) {
	  String[] partes = text.split("/");
    return LocalDate.of(Integer.parseInt(partes[2]), Integer.parseInt(partes[1]), Integer.parseInt(partes[0]));
  }

  private Editora buscaOuCriaEditora(String text) {
	  RepositorioEditora repoEditora = Main.getGerenciadorRepositorio().getRepositorio(RepositorioEditora.class);
    String nomeEditora = fieldEditora.getText();
    Editora editora = repoEditora.buscarPeloNome(nomeEditora);
    
    if (editora == null) {
      Alert alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Aviso");
      alert.setHeaderText(null);
      alert.setContentText(String.format("Não existe uma editora chamada '%s'.\nDeseja cadastrar uma nova editora com esse nome?", nomeEditora));
      alert.getButtonTypes().clear();
      alert.getButtonTypes().add(ButtonType.YES);
      alert.getButtonTypes().add(ButtonType.NO);
      
      Optional<ButtonType> respostaOpt = alert.showAndWait();

      // Se não responder ou responder NÃO, o livro não será cadastrado.
      if (!respostaOpt.isPresent() || respostaOpt.get() == ButtonType.NO) {
        return null;
      }
      
      editora = new Editora(nomeEditora);
      repoEditora.cadastrar(editora); // TODO: usar try..catch e mostrar Alerta caso aconteça algum erro...
    }

    return editora;
  }
	
}
