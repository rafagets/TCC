package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.ComentarioAvaliacao;
import es.esy.rafaelsilva.tcc.modelo.CurtidaAvaliacao;
import es.esy.rafaelsilva.tcc.modelo.CurtidaCompra;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 05/11/2016, enjoy it.
 */
public class AvaliacaoSP {
    private Context contexto;
    private String key;

    public AvaliacaoSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(Post post,
                       Avaliacao avaliacao,
                       Produto produto,
                       Usuario usuario,
                       List<CurtidaAvaliacao> curtidas,
                       List<ComentarioAvaliacao> comentarios){

        SharedPreferences save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(post);
        saveEdit.putString("POST", jsonPost);
        String jsonUsuario = gson.toJson(usuario);
        saveEdit.putString("USUARIO", jsonUsuario);
        String jsonAvaliacao = gson.toJson(avaliacao);
        saveEdit.putString("AVALIACAO", jsonAvaliacao);
        String jsonProduto= gson.toJson(produto);
        saveEdit.putString("PRODUTO", jsonProduto);
        String jsonComentarios = gson.toJson(comentarios);
        saveEdit.putString("COMENTARIOS", jsonComentarios);
        String jsonCurtidas = gson.toJson(curtidas);
        saveEdit.putString("CURTIDAS", jsonCurtidas);

        saveEdit.apply();
    }

    public Post lerPost(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("POST", null);
        return gson.fromJson(json, Post.class);
    }

    public Usuario lerUsuario(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("USUARIO", null);
        return gson.fromJson(json, Usuario.class);
    }

    public Produto lerProduto(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("PRODUTO", null);
        return gson.fromJson(json, Produto.class);
    }

    public Avaliacao lerAvaliacao(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("AVALIACAO", null);
        return gson.fromJson(json, Avaliacao.class);
    }

    public  List<CurtidaAvaliacao> lerCurtidas(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("CURTIDAS", null);
        Type type = new TypeToken<List<CurtidaAvaliacao>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public  List<ComentarioAvaliacao> lerComentarios(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("COMENTARIOS", null);
        Type type = new TypeToken<List<ComentarioAvaliacao>>(){}.getType();
        return gson.fromJson(json, type);
    }

}
