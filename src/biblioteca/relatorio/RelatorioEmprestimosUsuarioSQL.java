package biblioteca.relatorio;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RelatorioEmprestimosUsuarioSQL {

  private Connection conn;

  public RelatorioEmprestimosUsuarioSQL(Connection conn) {
    this.conn = conn;
  }

  public void gerarEmArquivo(File arquivo) {
    try {
      String sql =
        "select \n" +
          "  usuario.nome as nome_usuario,\n" +
          "  case when qtd_emprestimos is null\n" +
          "    then 0\n" +
          "    else qtd_emprestimos\n" +
          "  end,\n" +
          "  coalesce(\n" +
          "    (select \n" +
          "        date_part('year', data_emprestou) as ano\n" +
          "      from \n" +
          "        emprestimo e \n" +
          "      where \n" +
          "        e.id_usuario = usuario.id_usuario\n" +
          "      group by ano\n" +
          "      order by ano desc limit 1\n" +
          "    ), 0) as ano_mais_ativo\n" +
          "from usuario\n" +
          "full outer join (\n" +
          "  select \n" +
          "    id_usuario, count(*) as qtd_emprestimos \n" +
          "  from \n" +
          "    emprestimo e \n" +
          "  group by \n" +
          "    id_usuario\n" +
          ") tb on (usuario.id_usuario = tb.id_usuario) \n" +
          "order by qtd_emprestimos desc;";

      PrintWriter pw = new PrintWriter(new FileWriter(arquivo));

      try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        ResultSet rs = stmt.executeQuery();

        pw.println("-- Informações de Emprestimos de Cada Usuário --");
        pw.println();

        pw.println("N° de Emprestimos  -  Usuário  - Ano mais ativo");
        pw.println();

        while (rs.next()) {
          pw.print(rs.getInt("qtd_emprestimos"));
          pw.print("  -  ");
          pw.print(rs.getString("nome_usuario"));
          pw.print("  -  ");
          pw.print(rs.getString("ano_mais_ativo"));

          pw.println();
        }

        pw.close();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
