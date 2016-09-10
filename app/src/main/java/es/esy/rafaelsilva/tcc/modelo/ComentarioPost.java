package es.esy.rafaelsilva.tcc.modelo;

public class ComentarioPost {

	private String data;

	private int status;

	private String comentario;

	private Usuario usuario;

	private Comentario post;

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

	public Comentario getPost() {
		return post;
	}

	public void setPost(Comentario post) {
		this.post = post;
	}
}
