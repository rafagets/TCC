package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.ComentariosPostAdapter;
import es.esy.rafaelsilva.tcc.controle.CtrlComentarioPost;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class ComentariosPostActivity extends AppCompatActivity {

    private ProgressBar bar;
    private Context contexto = this;
    private List<ComentarioPost> listaComentarios;
    private EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // acrecentar tambem overite no fim

        setTitle("Comentários");
        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        final int codigoPost = getIntent().getIntExtra("post", 0);
        final int pai = getIntent().getIntExtra("pai", 0);

        loadComentarios(codigoPost);

        edit = (EditText) findViewById(R.id.txtComentario);

        edit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == keyEvent.ACTION_DOWN)
                    if (i == KeyEvent.KEYCODE_ENTER){
                        comentar(codigoPost, pai);
                    }
                return false;
            }
        });

    }

    private void comentar(final int codigoPost, int pai) {
        String campos = "comentario,usuario,coment";
        String values = "\""+edit.getText().toString() + "\"," + DadosUsuario.codigo + "," +codigoPost;
        new CtrlComentarioPost(ComentariosPostActivity.this).comentar(values, campos, pai, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                edit.setText("");
                loadComentarios(codigoPost);
            }

            @Override
            public void falha() {
                Toast.makeText(ComentariosPostActivity.this, "Erro ao comentar\nTente novamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadComentarios(int codigoPost) {
        new CtrlComentarioPost(this).listar("WHERE coment = " + codigoPost + " ORDER BY data ASC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaComentarios = new ArrayList<>();
                for (Object obj : lista)
                    listaComentarios.add((ComentarioPost) obj);

                getUsuario(0);
            }

            @Override
            public void falha() {
                bar.setVisibility(View.GONE);
            }
        });
    }

    private void getUsuario(final int posicao) {
        if (posicao <= listaComentarios.size() - 1) {
            new CtrlUsuario(this).trazer(listaComentarios.get(posicao).getUsuario(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    listaComentarios.get(posicao).setUsoarioObj((Usuario) obj);
                    getUsuario(posicao + 1);
                }

                @Override
                public void falha() {

                }
            });
        }else{
            montar();
        }
    }

    private void montar() {
        ComentariosPostAdapter adapter = new ComentariosPostAdapter(listaComentarios, contexto);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(contexto);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        bar.setVisibility(View.GONE);
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
