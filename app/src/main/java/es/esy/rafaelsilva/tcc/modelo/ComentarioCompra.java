package es.esy.rafaelsilva.tcc.modelo;

public class ComentarioCompra {

	/**
	 * data : 2016-09-05 11:57:00
	 * status : 1
	 * comentario : Que bom!
	 * compra : 1
	 * usuario : 1
	 */

	private String data;
	private int status;
	private String comentario;
	private int compra;
	private int usuario;

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

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public int getCompra() {
		return compra;
	}

	public void setCompra(int compra) {
		this.compra = compra;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
}
