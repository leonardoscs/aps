package biblioteca.relatorio;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RelatorioMaisEmprestadosSQL {

  private Connection conn;

  public RelatorioMaisEmprestadosSQL(Connection conn) {
    this.conn = conn;
  }

  public void gerarEmArquivo(File arquivo) {
    try {
      String sql =
        "select \n" +
        "  titulo, \n" +
        "  editora.nome as nome_editora,\n" +
        "  qtd_emprestimos,\n" +
        "  (select string_agg(autor.nome, ', ') as autores from livro_autor la join autor on (la.id_autor = autor.id_autor) where la.id_livro = livro.id_livro) as autores\n" +
        "from livro \n" +
        "join (\n" +
        "  select \n" +
        "    id_livro, count(*) as qtd_emprestimos \n" +
        "  from \n" +
        "    emprestimo e \n" +
        "  join \n" +
        "    exemplar_livro el on (el.id_exemplar = e.id_exemplar) \n" +
        "  where \n" +
        "    date_part('year', data_emprestou) = date_part('year', CURRENT_DATE) - 1" +
        "  group by \n" +
        "    id_livro\n" +
        ") tb on (livro.id_livro = tb.id_livro) \n" +
        "join \n" +
        "  editora on (livro.id_editora = editora.id_editora)\n" +
        "order by qtd_emprestimos desc \n" +
        "limit 20;";

      PrintWriter pw = new PrintWriter(new FileWriter(arquivo));

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();

        pw.println("-- Livros mais emprestados no último ano --");
        pw.println();

        pw.println("N° de Emprestimos  -  Título  -  Editora  -  Autores");
        pw.println();

        while (rs.next()) {
          pw.print(rs.getInt("qtd_emprestimos"));
          pw.print("  -  ");
          pw.print(rs.getString("titulo"));
          pw.print("  -  ");
          pw.print(rs.getString("nome_editora"));
          pw.print("  -  ");
          pw.print(rs.getString("autores"));

          pw.println();
        }

        pw.close();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
