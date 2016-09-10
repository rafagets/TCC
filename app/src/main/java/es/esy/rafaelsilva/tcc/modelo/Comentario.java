package es.esy.rafaelsilva.tcc.modelo;

public class Comentario {

	private int codigo;

	private String data;

	private int status;

	private String comentario;

	private Usuario usuario;

	private CurtidaComentario[] curtidaComentario;

	private ComentarioPost[] comentariosPost;

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public CurtidaComentario[] getCurtidaComentario() {
		return curtidaComentario;
	}

	public void setCurtidaComentario(CurtidaComentario[] curtidaComentario) {
		this.curtidaComentario = curtidaComentario;
	}

	public ComentarioPost[] getComentariosPost() {
		return comentariosPost;
	}

	public void setComentariosPost(ComentarioPost[] comentariosPost) {
		this.comentariosPost = comentariosPost;
	}
}
