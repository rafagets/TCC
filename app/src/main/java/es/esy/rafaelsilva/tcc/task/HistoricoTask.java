package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import es.esy.rafaelsilva.tcc.activity.ComentariosPostActivity;
import es.esy.rafaelsilva.tcc.activity.HistoricoActivity;
import es.esy.rafaelsilva.tcc.adapters.HistoricoAdapter;
import es.esy.rafaelsilva.tcc.adapters.ReputacaoProdutoAdapter;
import es.esy.rafaelsilva.tcc.dao.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.ComentariosPostAdapter;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.modelo.Tipo;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 08/09/2016.
 */
public class HistoricoTask extends AsyncTask<String, Void, Boolean> {

    private ProgressBar bar;
    private Context contexto;
    private List<Historico> lista;

    public HistoricoTask(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    protected void onPreExecute() {
//        bar = (ProgressBar) ((HistoricoActivity) contexto).findViewById(R.id.progressBar);
//        bar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(String... values) {

        String[] params;
        JSONArray jsonArray;
        DAO helper;

        params = new String[] { "acao", "tabela","condicao","valores", "ordenacao" };

        helper = new DAO();

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, params, values);

            try {
                lista = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String json = jsonArray.get(i).toString();

                    Historico obj;
                    Gson gson = new Gson();
                    obj = gson.fromJson(json, Historico.class);

                    obj.setTipoObj(loadTipo(obj.getTipo()));

                    lista.add(obj);
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

        //bar.setVisibility(View.GONE);

        LinearLayout layout = (LinearLayout) ((HistoricoActivity) contexto).findViewById(R.id.relativeLayout);;
        //for (Historico h : lista){
        for (int i = 0; i < lista.size(); i++){
            Historico h = lista.get(i);
            View v = ((HistoricoActivity) contexto).getLayoutInflater().inflate(R.layout.adapter_historico, null);

            TextView nome, data, detalhe;
            ImageView imgHistorico, fim;

            nome = (TextView) v.findViewById(R.id.lbNome);
            data = (TextView) v.findViewById(R.id.lbData);
            detalhe = (TextView) v.findViewById(R.id.lbDetalhes);
            fim = (ImageView) v.findViewById(R.id.imgFim);
            imgHistorico = (ImageView) v.findViewById(R.id.imgHistorico);

            nome.setText(h.getTipoObj().getNome());
            data.setText(Util.formatDataDDmesYYYY(h.getData()));
            detalhe.setText(h.getNome());

            new ImageLoaderTask(imgHistorico).execute(Config.caminhoImageIcons + h.getTipoObj().getImagem());

            layout.addView(v);

            if (lista.size() == i+1)
                fim.setVisibility(View.VISIBLE);

        }



    }



    private Tipo loadTipo(int codigo) {

        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "tipo", "codigo",  String.valueOf(codigo)};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);

            try {

                String json = jsonArray.get(0).toString();

                Tipo obj;
                Gson gson = new Gson();
                obj = gson.fromJson(json, Tipo.class);

                return obj;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){

        }

        return null;
    }

}
