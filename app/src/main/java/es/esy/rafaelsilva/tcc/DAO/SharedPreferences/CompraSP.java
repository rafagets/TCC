package es.esy.rafaelsilva.tcc.DAO.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import es.esy.rafaelsilva.tcc.modelo.Compra;
import es.esy.rafaelsilva.tcc.modelo.Mercado;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Produtor;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 17/11/2016, enjoy it.
 */
public class CompraSP {
    private Context contexto;
    private String key;

    public CompraSP(Context contexto, String key) {
        this.contexto = contexto;
        this.key = key;
    }

    public void salvar(Compra compra,
                       Post post,
                       Usuario usuario,
                       Mercado mercado,
                       Produto produto,
                       Produtor produtor){

        SharedPreferences save = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        SharedPreferences.Editor saveEdit = save.edit();
        Gson gson = new Gson();

        String jsonPost = gson.toJson(post);
        saveEdit.putString("POST", jsonPost);
        String jsonUsuario = gson.toJson(usuario);
        saveEdit.putString("USUARIO", jsonUsuario);
        String jsonGaleria = gson.toJson(mercado);
        saveEdit.putString("MERCADO", jsonGaleria);
        String jsonCompra = gson.toJson(compra);
        saveEdit.putString("COMPRA", jsonCompra);
        String jsonProduto = gson.toJson(produto);
        saveEdit.putString("PRODUTO", jsonProduto);
        String jsonProdutor = gson.toJson(produtor);
        saveEdit.putString("PRODUTOR", jsonProdutor);

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

    public Compra lerCompra(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("COMPRA", null);
        return gson.fromJson(json, Compra.class);
    }

    public Mercado lerMercado(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("MERCADO", null);
        return gson.fromJson(json, Mercado.class);
    }

    public Produto lerProduto(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("PRODUTO", null);
        return gson.fromJson(json, Produto.class);
    }

    public Produtor lerProdutor(){
        SharedPreferences read = contexto.getSharedPreferences(key, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = read.getString("PRODUTOR", null);
        return gson.fromJson(json, Produtor.class);
    }
}
