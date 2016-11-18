package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.esy.rafaelsilva.tcc.modelo.GaleriaImgUsuario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 15/11/2016, enjoy it.
 */
public class PostFotoSP {
    private Context contexto;
    private String key;

    public PostFotoSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(GaleriaImgUsuario galeria,
                       Post post,
                       Usuario usuario){

        SharedPreferences save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(post);
        saveEdit.putString("POST", jsonPost);
        String jsonUsuario = gson.toJson(usuario);
        saveEdit.putString("USUARIO", jsonUsuario);
        String jsonGaleria = gson.toJson(galeria);
        saveEdit.putString("GALERIA", jsonGaleria);

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

    public GaleriaImgUsuario lerGaleria(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("GALERIA", null);
        return gson.fromJson(json, GaleriaImgUsuario.class);
    }

}
