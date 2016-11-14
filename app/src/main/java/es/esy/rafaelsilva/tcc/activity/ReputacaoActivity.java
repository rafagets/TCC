package es.esy.rafaelsilva.tcc.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.task.ReputacaoTask;

public class ReputacaoActivity extends AppCompatActivity {

    private SwipeRefreshLayout recarregar;
    public Bitmap icon;
    public Produto produto;
    ImageView btLocal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reputacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("nome"));
        btLocal = (ImageView) findViewById(R.id.btLocalizacao);
        btLocal.setOnClickListener(verMapa());
        Bundle extras = getIntent().getExtras();
        if (extras != null)
            produto = (Produto) getIntent().getSerializableExtra("obj");


        final CircleImageView icone = (CircleImageView) findViewById(R.id.imgIcone);

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
    }

    private View.OnClickListener verMapa() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ReputacaoActivity.this, Mapa_Activity.class);
////                intent.putExtra()
//                startActivity(intent);
            }

        };
    }

    public  void loadAvaliacoes(){
        ReputacaoTask task = new ReputacaoTask(ReputacaoActivity.this, recarregar, produto);
        task.execute();
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
