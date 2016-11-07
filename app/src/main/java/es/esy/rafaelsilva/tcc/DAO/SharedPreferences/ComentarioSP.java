package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 05/11/2016, enjoy it.
 */
public class ComentarioSP {

    private Context contexto;
    private String key;

    public ComentarioSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(List<CurtidaComentario> cc, Comentario comentario, List<ComentarioPost> cp, Post post, Usuario usuario){

        SharedPreferences  save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(post);
        saveEdit.putString("POST", jsonPost);
        String jsonUsuario = gson.toJson(usuario);
        saveEdit.putString("USUARIO", jsonUsuario);
        String jsonComentario = gson.toJson(comentario);
        saveEdit.putString("COMENTARIO", jsonComentario);
        String jsonListaCC = gson.toJson(cc);
        saveEdit.putString("CC", jsonListaCC);
        String jsonListaCP = gson.toJson(cp);
        saveEdit.putString("CP", jsonListaCP);

        saveEdit.apply();

    }

    public Post lerPost(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("POST", null);
        return gson.fromJson(json, Post.class);
    }

    public Comentario lerComentario(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("COMENTARIO", null);
        return gson.fromJson(json, Comentario.class);
    }

    public Usuario lerUsuario(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("USUARIO", null);
        return gson.fromJson(json, Usuario.class);
    }

    public List<CurtidaComentario> lerCC(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("CC", null);
        Type type = new TypeToken<List<CurtidaComentario>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public List<ComentarioPost> lerCP(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("CP", null);
        Type type = new TypeToken<List<ComentarioPost>>(){}.getType();
        return gson.fromJson(json, type);
    }

}
