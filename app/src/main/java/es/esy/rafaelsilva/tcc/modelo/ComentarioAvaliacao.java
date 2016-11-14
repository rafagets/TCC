package es.esy.rafaelsilva.tcc.modelo;

public class ComentarioAvaliacao {


	/**
	 * codigo : 1
	 * data : 2016-09-05 11:49:57
	 * status : 1
	 * comentario : Bom saber...
	 * usuario : 4
	 * avaliacao : 1
	 */

	private int codigo;
	private String data;
	private int status;
	private String comentario;
	private int usuario;
	private int avaliacao;
	private Usuario usuarioObj;

	public Usuario getUsuarioObj() {
		return usuarioObj;
	}

	public void setUsuarioObj(Usuario usuarioObj) {
		this.usuarioObj = usuarioObj;
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

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public int getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(int avaliacao) {
		this.avaliacao = avaliacao;
	}
}
