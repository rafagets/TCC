package es.esy.rafaelsilva.tcc.modelo;

public class Produto {


	/**
	 * codigo : 2
	 * nome : Maca Gala
	 * descricao : Maca de otima qualidade
	 * status : 1
	 * imgicone : maca.jpg
	 * imgheader : maca_header.jpg
	 * produtor : 9
	 * categoria : 1
	 */

	private int codigo;
	private String nome;
	private String descricao;
	private int status;
	private String imgicone;
	private String imgheader;
	private int produtor;
	private int categoria;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getImgicone() {
		return imgicone;
	}

	public void setImgicone(String imgicone) {
		this.imgicone = imgicone;
	}

	public String getImgheader() {
		return imgheader;
	}

	public void setImgheader(String imgheader) {
		this.imgheader = imgheader;
	}

	public int getProdutor() {
		return produtor;
	}

	public void setProdutor(int produtor) {
		this.produtor = produtor;
	}

	public int getCategoria() {
		return categoria;
	}

	public void setCategoria(int categoria) {
		this.categoria = categoria;
	}
}
