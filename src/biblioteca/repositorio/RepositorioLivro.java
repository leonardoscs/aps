package biblioteca.repositorio;

import biblioteca.entidades.Autor;
import biblioteca.entidades.Categoria;
import biblioteca.entidades.Livro;

import java.util.List;

public interface RepositorioLivro extends Repositorio<Livro> {

  void adicionarAutor(Livro livro, Autor autor);
  void removerAutor(Livro livro, Autor autor);
  
  void adicionarCategoria(Livro livro, Categoria categoria);
  void removerCategoria(Livro livro, Categoria categoria);

  List<Livro> buscarPeloTituloParcial(String titulo);

  List<Livro> buscarPeloAutor(Autor autor);

  int calcularQuantidadeDeLivrosCadastrados();
}
