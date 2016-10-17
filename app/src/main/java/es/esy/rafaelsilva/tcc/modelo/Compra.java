package es.esy.rafaelsilva.tcc.modelo;

public class Compra {


	/**
	 * codigo : 1
	 * data : 2016-09-05 11:53:20
	 * status : 1
	 * notificacao : 1
	 * carater : 1
	 * usuario : 2
	 * produto : 3
	 * pai : 4
	 */

	private int codigo;
	private String data;
	private int status;
	private int notificacao;
	private int carater;
	private int usuario;
	private int produto;
	private int pai;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNotificacao() {
		return notificacao;
	}

	public void setNotificacao(int notificacao) {
		this.notificacao = notificacao;
	}

	public int getCarater() {
		return carater;
	}

	public void setCarater(int carater) {
		this.carater = carater;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public int getProduto() {
		return produto;
	}

	public void setProduto(int produto) {
		this.produto = produto;
	}

	public int getPai() {
		return pai;
	}

	public void setPai(int pai) {
		this.pai = pai;
	}
}
