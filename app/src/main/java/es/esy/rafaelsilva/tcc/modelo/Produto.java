package es.esy.rafaelsilva.tcc.modelo;

import android.content.Context;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

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

	public void setImgIcone(CircleImageView img, Context contexto){
		if (imgicone != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImageProdutos + imgicone)
					.placeholder(R.drawable.ic_produto)
					.error(R.drawable.ic_produto)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_produto);
		}
	}

	public void setImgFundo(ImageView img, Context contexto){
		if (imgheader != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImageProdutos + imgheader)
					.placeholder(R.drawable.ic_produto)
					.error(R.drawable.ic_produto)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_produto);
		}
	}

}
