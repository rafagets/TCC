package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.ReputacaoProdutoAdapter;
import es.esy.rafaelsilva.tcc.util.Config;

public class ReputacaoProdutoActivity extends AppCompatActivity {

    private List<Teste> lista;
    private Context contexto = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputacao_produto);

        RequestParams params = new RequestParams();
        params.put("acao", "R");
        params.put("tabela", "comentario");
        params.put("ordenacao", "ORDER BY data DESC");

        trazerDados(params);
    }

    private void trazerDados(RequestParams params) {

        String url = Config.urlMaster;
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar2);
        bar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(contexto, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                bar.setVisibility(View.GONE);

                String resposta =  new String(responseBody);
                Log.e("+++++", "resposta: "+ resposta);

                JSONArray array;
                lista = new ArrayList<>();

                try {
                    array = new JSONArray(resposta);
                    Teste teste;
                    for (int i = 0; i < array.length(); i++) {
                        String rr = array.get(i).toString();
                        Gson gson = new Gson();
                        teste = gson.fromJson(rr, Teste.class);
                        lista.add(teste);

                        Log.e("+++++", "Valor: "+ teste.getComentario() + " codigo: "+ teste.getCodigo());
                    }

                    popularRecyclerView();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                bar.setVisibility(View.GONE);
                Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
            }
        });

    }


    public void popularRecyclerView(){

        ReputacaoProdutoAdapter adapter = new ReputacaoProdutoAdapter(ReputacaoProdutoActivity.this, lista);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(ReputacaoProdutoActivity.this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }

}
