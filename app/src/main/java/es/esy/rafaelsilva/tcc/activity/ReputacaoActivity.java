package es.esy.rafaelsilva.tcc.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.views.ViewAvaliacao;

public class ReputacaoActivity extends AppCompatActivity {

    private SwipeRefreshLayout recarregar;
    public Bitmap icon;
    public Produto produto;
    private List<Post> posts;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("nome"));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            produto = (Produto) getIntent().getSerializableExtra("obj");


        final CircleImageView icone = (CircleImageView) findViewById(R.id.imgIcone);
        // implementação do historico
        layout = (LinearLayout) findViewById(R.id.container);

        icon = getIntent().getParcelableExtra("icone");
        icone.setImageBitmap(icon);

        recarregar = (SwipeRefreshLayout) findViewById(R.id.recarregar);
        recarregar.setRefreshing(true);

        this.loadAvaliacoes();

        recarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAvaliacoes();
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

        fab.setVisibility(View.GONE);
    }

    public  void loadAvaliacoes(){
        getPost();
    }

    public void getPost(){
        String in = "";
        for (Avaliacao av : produto.getListaAvaliacao()) {
            in += av.getPai()+",";
        }

        // retira ultima virgula
        in = in.substring(0, in.lastIndexOf(","));

        new CtrlPost(this).listar("WHERE codigo IN(" + in + ")", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                posts = new ArrayList<>();
                for (Object obj : lista)
                    posts.add((Post) obj);

                montar(0);
            }

            @Override
            public void falha() {

            }
        });
    }

    public void montar(final int posicao){
        if (posicao <= posts.size() - 1) {
            if (posts.get(posicao).getTipo() == 3) {
                View view = getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);
                new ViewAvaliacao(this, view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montar(posicao + 1);
                        } else {
                            Log.e("*** ERRO", "Erro inserir post[" + posicao + "]-> codigo [" + posts.get(posicao).getCodigo() + "]");
                            montar(posicao + 1);
                        }
                    }
                });
            }else {
                montar(posicao + 1);
            }
        }else{
            recarregar.setRefreshing(false);
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
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
