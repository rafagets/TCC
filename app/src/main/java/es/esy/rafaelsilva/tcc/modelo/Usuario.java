package es.esy.rafaelsilva.tcc.modelo;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.util.Config;

public class Usuario {

	private int codigo;

	private String nome;

	private String sobrenome;

	private String email;

	private String senha;

	private String rua;

	private int numero;

	private int cep;

	private String dataNasc;

	private int status;

	private String imagem;

	private String imgBitmap;
	private int tipoImg;

	private String profissao;

	private String alimentacao;

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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCep() {
		return cep;
	}

	public void setCep(int cep) {
		this.cep = cep;
	}

	public String getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public String getImgBitmap() {
		return imgBitmap;
	}

	public void setImgBitmap(String imgBitmap) {
		this.imgBitmap = imgBitmap;
	}

	public int getTipoImg() {
		return tipoImg;
	}

	public void setTipoImg(int tipoImg) {
		this.tipoImg = tipoImg;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getAlimentacao() {
		return alimentacao;
	}

	public void setAlimentacao(String alimentacao) {
		this.alimentacao = alimentacao;
	}

	public Usuario getUsuarioObj(int codigo){

		JSONArray jsonArray;
		DAO helper = new DAO();

		String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
		String[] v = new String[] { "R", "usuario", "codigo",  String.valueOf(codigo)};

		try {
			jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
			String json = jsonArray.get(0).toString();

			Usuario obj;
			Gson gson = new Gson();
			obj = gson.fromJson(json, Usuario.class);
			return obj;

		}catch (Exception e){

		}

		return null;

	}

	public Usuario getUsuarioObjEmail(String email){

		JSONArray jsonArray;
		DAO helper = new DAO();

		String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
		String[] v = new String[] { "R", "usuario", "email", email};

		try {
			jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
			String json = jsonArray.get(0).toString();

			Usuario obj;
			Gson gson = new Gson();
			obj = gson.fromJson(json, Usuario.class);
			System.out.println("TESTE>>>Codigo: "+obj.getCodigo()+"\nNome: " + obj.getNome() + "\nEmail: " + obj.getEmail() + "\nSenha: " +
					obj.getSenha() + "\nProfissao: " + obj.getProfissao() + "\nAlimentação: " + obj.getAlimentacao());
			return obj;



		}catch (Exception e){
			e.printStackTrace();
		}

		return null;

	}

	public void setImagemPerfil(CircleImageView img, Context contexto){
		if (imagem != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImageTumb + imagem)
					.placeholder(R.drawable.ic_usuario)
					.error(R.drawable.ic_usuario)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_usuario);
		}
	}

	public void setImagemGrande(ImageView img, Context contexto){
		if (imagem != null) {
			Picasso.with(contexto)
					.load(Config.caminhoImageTumb + imagem)
					.placeholder(R.drawable.ic_usuario)
					.error(R.drawable.ic_usuario)
					.into(img);
		}else{
			img.setImageResource(R.drawable.ic_usuario);
		}
	}
}
