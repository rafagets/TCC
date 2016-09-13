package es.esy.rafaelsilva.tcc.modelo;

public class Historico {


	/**
	 * codigo : 1
	 * data : 2016-09-05 12:33:20
	 * cordenadas : -21.798179, -49.945812
	 * nome : Colheita
	 * produto : 2
	 * tipo : 1
	 */

	private int codigo;
	private String data;
	private String cordenadas;
	private String nome;
	private int lote;
	private int tipo;
	private Tipo tipoObj;

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

	public String getCordenadas() {
		return cordenadas;
	}

	public void setCordenadas(String cordenadas) {
		this.cordenadas = cordenadas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getProduto() {
		return lote;
	}

	public void setProduto(int produto) {
		this.lote = produto;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public Tipo getTipoObj() {
		return tipoObj;
	}

	public void setTipoObj(Tipo tipoObj) {
		this.tipoObj = tipoObj;
	}
}
