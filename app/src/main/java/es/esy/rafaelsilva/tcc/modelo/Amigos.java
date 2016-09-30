package es.esy.rafaelsilva.tcc.modelo;

public class Amigos {


	/**
	 * codigo : 1
	 * amigoAdd : 1
	 * amigoAce : 2
	 * statusAmizade : 1
	 * pai : 2
	 */

	private int codigo;
	private int amigoAdd;
	private int amigoAce;
	private int statusAmizade;
	private int pai;
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

	public int getAmigoAdd() {
		return amigoAdd;
	}

	public void setAmigoAdd(int amigoAdd) {
		this.amigoAdd = amigoAdd;
	}

	public int getAmigoAce() {
		return amigoAce;
	}

	public void setAmigoAce(int amigoAce) {
		this.amigoAce = amigoAce;
	}

	public int getStatusAmizade() {
		return statusAmizade;
	}

	public void setStatusAmizade(int statusAmizade) {
		this.statusAmizade = statusAmizade;
	}

	public int getPai() {
		return pai;
	}

	public void setPai(int pai) {
		this.pai = pai;
	}
}
