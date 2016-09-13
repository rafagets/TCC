package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.task.HistoricoTask;
import es.esy.rafaelsilva.tcc.util.Config;

public class HistoricoActivity extends AppCompatActivity {

    private List<Historico> lista;
    private Context contexto = this;
    private ProgressBar bar;
    private Lote lote;
    private Produto produto;

    public Lote getLote() {
        return lote;
    }

    public Produto getProduto() {
        return produto;
    }

    public ProgressBar getBar() {
        return bar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);
        String lote = String.valueOf(getIntent().getIntExtra("lote", 0));
        RequestParams params = new RequestParams();
        params.put("acao", "R");
        params.put("tabela", "lote");
        params.put("condicao", "codigo");
        params.put("valores", lote);

        this.getLotes(params);

        ImageView btSaude = (ImageView) findViewById(R.id.btSaude);
        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoricoActivity.this, DetalesProdutoActivity.class);
                startActivity(intent);
            }
        });

        ImageView btLocalizacao = (ImageView) findViewById(R.id.btLocalizacao) ;
        btLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getLotes(RequestParams params) {

        String url = Config.urlMaster;

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(contexto, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String resposta =  new String(responseBody);
                Log.e("+++++", "resposta: "+ resposta);

                JSONArray array;
                try {

                    array = new JSONArray(resposta);
                    String rr = array.get(0).toString();
                    Gson gson = new Gson();
                    lote = gson.fromJson(rr, Lote.class);

                    String produto = String.valueOf(lote.getProduto());
                    RequestParams params = new RequestParams();
                    params.put("acao", "R");
                    params.put("tabela", "produto");
                    params.put("condicao", "codigo");
                    params.put("valores", produto);

                    getProduto(params);

                } catch (JSONException e) {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(contexto, "Falha ao carregar lote", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void getProduto(RequestParams params){

        String url = Config.urlMaster;

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(contexto, url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String resposta =  new String(responseBody);
                Log.e("+++++", "resposta: "+ resposta);

                JSONArray array;
                try {

                    array = new JSONArray(resposta);
                    String rr = array.get(0).toString();
                    Gson gson = new Gson();
                    produto = gson.fromJson(rr, Produto.class);

                    setTitle(produto.getNome());
                    HistoricoTask loadPosts = new HistoricoTask(contexto);
                    loadPosts.execute("R", "historico","lote",String.valueOf(lote.getCodigo())," ORDER BY data ASC");

                } catch (JSONException e) {
                    bar.setVisibility(View.GONE);
                    Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
                    finish();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HistoricoActivity.this, HomeActivity.class);
        startActivity(intent);

        this.finish();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { // manipula o menu back
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
