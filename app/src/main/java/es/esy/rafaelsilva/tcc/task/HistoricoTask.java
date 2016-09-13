package es.esy.rafaelsilva.tcc.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.activity.HistoricoActivity;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;
import es.esy.rafaelsilva.tcc.dao.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.modelo.Tipo;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 08/09/2016.
 */
public class HistoricoTask extends AsyncTask<String, Void, Boolean> {

    private ProgressBar bar;
    private Context contexto;
    private List<Historico> lista;
    private Activity historico;
    private HistoricoActivity pai;

    public HistoricoTask(Context contexto) {
        this.contexto = contexto;
        this.bar = ((HistoricoActivity) contexto).getBar();
        this.historico = ((HistoricoActivity) contexto);
        this.pai = ((HistoricoActivity) contexto);
    }

    @Override
    protected void onPreExecute() {

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

        ImageView fundo = (ImageView) historico.findViewById(R.id.imgFundo);
        CircleImageView icone = (CircleImageView) historico.findViewById(R.id.imgIcone);
        TextView produto, produtor;

        produto = (TextView) historico.findViewById(R.id.lbProduto);
        produto.setText(pai.getProduto().getNome());

        produtor = (TextView) historico.findViewById(R.id.lbProdutor);
        //produtor.setText(pai.getProduto().getNome());

        new ImageLoaderTask(fundo).execute(Config.caminhoImageProdutos + pai.getProduto().getImgheader());
        new ImageLoaderTask(icone).execute(Config.caminhoImageProdutos + pai.getProduto().getImgicone());



        // implementação do historico
        RelativeLayout view = (RelativeLayout) ((HistoricoActivity) contexto).findViewById(R.id.view);
        LinearLayout layout = (LinearLayout) ((HistoricoActivity) contexto).findViewById(R.id.relativeLayout);;
        //for (Historico h : lista){
        for (int i = 0; i < lista.size(); i++){
            Historico h = lista.get(i);
            View v = ((HistoricoActivity) contexto).getLayoutInflater().inflate(R.layout.inflater_historico, null);

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

        bar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);

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
