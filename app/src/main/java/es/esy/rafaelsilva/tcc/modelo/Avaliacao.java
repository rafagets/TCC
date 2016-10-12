package es.esy.rafaelsilva.tcc.modelo;

public class Avaliacao {


	/**
	 * codigo : 1
	 * estrelas : 3.5
	 * data : 2016-09-05 11:41:58
	 * status : 1
	 * comentario : Produto Exelente!
	 * usuario : 1
	 * produto : 2
	 * pai : 2
	 */

	private int codigo;
	private float estrelas;
	private String data;
	private int status;
	private String comentario;
	private int usuario;
	private Usuario usuarioObj;
	private int produto;
	private Produto produtoObj;
	private int pai;

	public Produto getProdutoObj() {
		return produtoObj;
	}

	public void setProdutoObj(Produto produtoObj) {
		this.produtoObj = produtoObj;
	}

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

	public float getEstrelas() {
		return estrelas;
	}

	public void setEstrelas(float estrelas) {
		this.estrelas = estrelas;
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
