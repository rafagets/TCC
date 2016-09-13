package es.esy.rafaelsilva.tcc.modelo;

public class Lote {


	/**
	 * codigo : 2
	 * data : 2016-09-13 16:45:21
	 * datavencimento : 0000-00-00
	 * status : 1
	 * setor : 1
	 * produto : 2
	 */

	private int codigo;
	private String data;
	private String datavencimento;
	private int status;
	private int setor;
	private int produto;
	private Produto produtoObj;

	public Produto getProdutoObj() {
		return produtoObj;
	}

	public void setProdutoObj(Produto produtoObj) {
		this.produtoObj = produtoObj;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getDatavencimento() {
		return datavencimento;
	}

	public void setDatavencimento(String datavencimento) {
		this.datavencimento = datavencimento;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSetor() {
		return setor;
	}

	public void setSetor(int setor) {
		this.setor = setor;
	}

	public int getProduto() {
		return produto;
	}

	public void setProduto(int produto) {
		this.produto = produto;
	}
}
