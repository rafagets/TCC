package es.esy.rafaelsilva.tcc.modelo;

public class ComentarioPost {


	/**
	 * codigo : 1
	 * data : 2016-09-05 11:57:57
	 * status : 1
	 * comentario : so voce mesmo...
	 * coment : 3
	 * usuario : 1
	 */

	private int codigo;
	private String data;
	private int status;
	private String comentario;
	private int coment;
	private int usuario;
	private Usuario usoarioObj;

	public Usuario getUsoarioObj() {
		return usoarioObj;
	}

	public void setUsoarioObj(Usuario usoarioObj) {
		this.usoarioObj = usoarioObj;
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

	public int getComent() {
		return coment;
	}

	public void setComent(int coment) {
		this.coment = coment;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}
}
