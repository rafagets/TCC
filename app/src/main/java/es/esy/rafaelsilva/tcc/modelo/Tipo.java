package es.esy.rafaelsilva.tcc.modelo;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.util.Config;

public class Tipo {


	/**
	 * codigo : 1
	 * nome : Plantio
	 * imagem : plantio.png
	 */

	private int codigo;
	private String nome;
	private String imagem;

	public void setImagem(ImageView img, Context contexto){
		if (imagem != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImageIcons + imagem)
					.placeholder(R.drawable.ic_usuario)
					.error(R.drawable.ic_usuario)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_usuario);
		}
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

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}
