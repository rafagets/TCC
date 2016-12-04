package es.esy.rafaelsilva.tcc.modelo;

public class Detalhes {

	/**
	 * infGerais : Maçã fabricada com alto controle de qualidade. Nossa maça é livre de agrotoxicos
	 * tabNutricional : Tabela nutricional da Maçã:
	 % VD (*)
	 Calorias (valor energético)	84,76 kcal	4,24 %
	 Pontos*	2	-
	 Carboidratos	19,89 g	6,63 %
	 Proteínas	0,25 g	0,33 %
	 Gorduras totais	0,47 g	0,85 %
	 Gorduras saturadas	0,08 g	0,35 %
	 Fibra alimentar	2,56 g	10,24 %
	 Sódio	0 mg	0 %
	 (*) % Valores Diários de referência com base em uma dieta de 2.000 kcal ou 8400 kJ. Seus valores diários podem ser maiores ou menores dependendo de suas necessidades energéticas.

	 * produto : 2
	 */

	private String infGerais;
	private String tabNutricional;
	private int produto;

	public String getInfGerais() {
		return infGerais;
	}

	public void setInfGerais(String infGerais) {
		this.infGerais = infGerais;
	}

	public String getTabNutricional() {
		return tabNutricional;
	}

	public void setTabNutricional(String tabNutricional) {
		this.tabNutricional = tabNutricional;
	}

	public int getProduto() {
		return produto;
	}

	public void setProduto(int produto) {
		this.produto = produto;
	}
}
