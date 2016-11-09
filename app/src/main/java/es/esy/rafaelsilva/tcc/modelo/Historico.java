package es.esy.rafaelsilva.tcc.modelo;

public class Historico {


	/**
	 * codigo : 1
	 * data : 2016-09-05 12:33:20
	 * latitude : -21.798179
	 * logitude: -49.945812
	 * nome : Colheita
	 * produto : 2
	 * tipo : 1
	 */

	private int codigo;
	private String data;
	private double latitude;
	private double longitude;
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

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getLote() {
		return lote;
	}

	public void setLote(int lote) {
		this.lote = lote;
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
