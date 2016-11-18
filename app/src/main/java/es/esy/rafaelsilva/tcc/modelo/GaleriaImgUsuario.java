package es.esy.rafaelsilva.tcc.modelo;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.util.Config;

public class GaleriaImgUsuario {

	/**
	 * codigo : 1
	 * img : teste.jpg
	 * legenda : teste teste
	 * status : 0
	 * data : 2016-11-15 23:14:54
	 * usuario : 20
	 * pai : 4
	 */

	private int codigo;
	private String img;
	private String legenda;
	private int status;
	private String data;
	private int usuario;
	private int pai;

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getLegenda() {
		return legenda;
	}

	public void setLegenda(String legenda) {
		this.legenda = legenda;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public int getPai() {
		return pai;
	}

	public void setPai(int pai) {
		this.pai = pai;
	}


	public void setFoto(ImageView img, Context contexto){
		if (img != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImagePosts + this.img)
					.placeholder(R.drawable.ic_img_post)
					.error(R.drawable.ic_img_post)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_img_post);
		}
	}
}
