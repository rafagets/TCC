package es.esy.rafaelsilva.tcc.modelo;

public class Mercado {


	/**
	 * codigo : 1
	 * nome : Avenida
	 * endereco : Rua Floriano Peixoto, 2040 - Centro, Lins - SP, 16400-101
	 * cordenadas : -21.678557, -49.766342
	 * cidade : 2
	 */

	private int codigo;
	private String nome;
	private String endereco;
	private String cordenadas;
	private String cidade;

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

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCordenadas() {
		return cordenadas;
	}

	public void setCordenadas(String cordenadas) {
		this.cordenadas = cordenadas;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
}
