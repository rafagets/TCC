package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 05/11/2016, enjoy it.
 */

public class AmizadeSP {
    private Context contexto;
    private String key;

    public AmizadeSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(Post post, Amigos amigos, Usuario amiAce, Usuario amiAdd){

        SharedPreferences save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(post);
        saveEdit.putString("POST", jsonPost);
        String jsonAmigos = gson.toJson(amigos);
        saveEdit.putString("AMIGOS", jsonAmigos);
        String jsonAmiAce = gson.toJson(amiAce);
        saveEdit.putString("AMIACE", jsonAmiAce);
        String jsonAmiAdd = gson.toJson(amiAdd);
        saveEdit.putString("AMIADD", jsonAmiAdd);

        saveEdit.apply();

    }

    public Post lerPost(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("POST", null);
        return gson.fromJson(json, Post.class);
    }

    public Amigos lerAmigos(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("AMIGOS", null);
        return gson.fromJson(json, Amigos.class);
    }

    public Usuario lerAmiAce(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("AMIACE", null);
        return gson.fromJson(json, Usuario.class);
    }

    public Usuario lerAmiAdd(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("AMIADD", null);
        return gson.fromJson(json, Usuario.class);
    }

}
