package es.esy.rafaelsilva.tcc.modelo;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.util.Config;

public class Produto implements Serializable {


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
	private Produtor produtorObj;
	private int categoria;
	private List<Avaliacao> listaAvaliacao;

	public List<Avaliacao> getListaAvaliacao() {
		return listaAvaliacao;
	}

	public void setListaAvaliacao(List<Avaliacao> listaAvaliacao) {
		this.listaAvaliacao = listaAvaliacao;
	}

	public Produtor getProdutorObj() {
		return produtorObj;
	}

	public void setProdutorObj(Produtor produtorObj) {
		this.produtorObj = produtorObj;
	}

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

	public void setImgIcone(CircleImageView img){
		if (imgicone != null) {
			new ImageLoaderTask(img).execute(Config.caminhoImageTumb + imgicone);
		}else{
			img.setImageResource(R.drawable.ic_usuario);
			//img.setBackgroundColor(R.color.cardview_light_background);
		}
	}

	public void setImgFundo(CircleImageView img){
		if (imgheader != null) {
			new ImageLoaderTask(img).execute(Config.caminhoImageTumb + imgheader);
		}else{
			img.setImageResource(R.drawable.fundo);
			//img.setBackgroundColor(R.color.cardview_light_background);
		}
	}


	public Produto getProdutoObj(int codigo){

		JSONArray jsonArray;
		DAO helper = new DAO();

		String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
		String[] v = new String[] { "R", "produto", "codigo",  String.valueOf(codigo)};

		try {
			jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
			String json = jsonArray.get(0).toString();

			Produto obj;
			Gson gson = new Gson();
			obj = gson.fromJson(json, Produto.class);
			return obj;

		}catch (Exception e){

		}

		return null;

	}

}
