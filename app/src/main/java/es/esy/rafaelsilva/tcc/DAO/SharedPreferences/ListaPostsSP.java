package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import es.esy.rafaelsilva.tcc.modelo.Post;

/**
 * Criado por Rafael em 05/11/2016, enjoy it.
 */
public class ListaPostsSP {
    private Context contexto;
    private String key;

    public ListaPostsSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(List<Post> posts){
        SharedPreferences  save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(posts);
        saveEdit.putString("POST", jsonPost);

        saveEdit.apply();
    }

    public List<Post> lerPosts(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("POST", null);
        Type type = new TypeToken<List<Post>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
