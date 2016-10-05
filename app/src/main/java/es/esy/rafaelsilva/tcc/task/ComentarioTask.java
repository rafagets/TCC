package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.PostAdapter;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;

/**
 * Created by Rafael on 06/09/2016.
 */
public class ComentarioTask extends AsyncTask<String, Integer, Boolean> {

    private SwipeRefreshLayout recarregar;
    private Context contexto;
    private List<Comentario> comentarios;

    public ComentarioTask(Context contexto, SwipeRefreshLayout recarregar) {
        this.contexto = contexto;
        this.recarregar = recarregar;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... values) {

        String[] params;
        JSONObject jsonObject;
        JSONArray jsonArray;
        DAO helper;

        params = new String[] { "acao", "tabela", "ordenacao" };

        helper = new DAO();

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, params, values);

            try {
                comentarios = new ArrayList<Comentario>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    Comentario c = new Comentario();
                    c.setCodigo(jsonObject.getInt("codigo"));
                    c.setComentario(jsonObject.getString("comentario"));
                    c.setData(jsonObject.getString("data"));
                    c.setStatus(jsonObject.getInt("status"));

                    c.setUsuario(loadUsuario(String.valueOf(jsonObject.getInt("usuarioPost"))));

                    c.setCurtidaComentario(loadCurtidas(c.getCodigo()));

                    c.setComentariosPost(loadCoemntariosPost(String.valueOf(jsonObject.getInt("codigo"))));

                    comentarios.add(c);
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean flag) {

        recarregar.setRefreshing(false);

//        AdapterHome adapterHome = new AdapterHome(contexto, comentarios);
//        ListView listView = (ListView) ((HomeActivity) contexto).findViewById(R.id.lista);
//        listView.setAdapter(adapterHome);

//        PostAdapter adapter = new PostAdapter(comentarios, contexto);
//        RecyclerView recyclerView = (RecyclerView) ((HomeActivity) contexto).findViewById(R.id.lista);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(contexto);
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);

    }



    private ComentarioPost[] loadCoemntariosPost(String codigo) {

        JSONObject jsonObject;
        JSONArray jsonArray;
        DAO helper = new DAO();


        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "comentariopost", "coment",  String.valueOf(codigo)};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            ComentarioPost[] comentariosPost = new ComentarioPost[jsonArray.length()];

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    ComentarioPost u = new ComentarioPost();
                    u.setStatus(jsonObject.getInt("status"));
                    u.setComentario(jsonObject.getString("comentario"));
                    u.setData(jsonObject.getString("data"));

                    Usuario usu = new Usuario();
                    usu.setCodigo(jsonObject.getInt("usuario"));
                    u.setUsuario(usu);

                    Comentario c = new Comentario();
                    c.setCodigo(jsonObject.getInt("coment"));
                    u.setPost(c);

                    comentariosPost[i] = u;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return comentariosPost;

        }catch (Exception e){

        }

        return null;

    }


    private CurtidaComentario[] loadCurtidas(int codigo) {

        JSONObject jsonObject;
        JSONArray jsonArray;
        DAO helper = new DAO();


        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "curtidacomentario", "comentario",  String.valueOf(codigo)};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            CurtidaComentario[] curtida = new CurtidaComentario[jsonArray.length()];

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    CurtidaComentario u = new CurtidaComentario();
                    u.setComentario(jsonObject.getInt("comentario"));
                    u.setUsuario(jsonObject.getInt("usuario"));
                    u.setData(jsonObject.getString("data"));

                    curtida[i] = u;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return curtida;

        }catch (Exception e){

        }

        return null;
    }


    private Usuario loadUsuario(String codigo) {

        JSONObject jsonObject;
        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "usuario", "codigo",  codigo};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = (JSONObject) jsonArray.get(i);

                    Usuario u = new Usuario();
                    u.setCodigo(jsonObject.getInt("codigo"));
                    u.setNome(jsonObject.getString("nome"));
                    u.setImagem(jsonObject.getString("imagem"));

                    return u;
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }

        return null;
    }
}
