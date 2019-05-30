package model.entidades;

public class TipoUsuario {

	private int id;
	
	private String descricao;
	
	private int qtd_dia_emprestimo;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getQtd_dia_emprestimo() {
		return qtd_dia_emprestimo;
	}

	public void setQtd_dia_emprestimo(int qtd_dia_emprestimo) {
		this.qtd_dia_emprestimo = qtd_dia_emprestimo;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
}
