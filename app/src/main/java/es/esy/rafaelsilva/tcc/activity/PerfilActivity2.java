package es.esy.rafaelsilva.tcc.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CrtlAmigos;
import es.esy.rafaelsilva.tcc.controle.CrtlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CrtlPost;
import es.esy.rafaelsilva.tcc.controle.CrtlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

public class PerfilActivity2 extends AppCompatActivity {
    private SwipeRefreshLayout recarregar;
    Usuario usuario;
    List<Post> listaPost;
    List<Amigos> listaAmigos;

    // dados usuario logado
    private TextView nome, profissao, estilo, totalAmigos, totalAvaliacoes;
    CircleImageView imgUsuario;

    // Layout que sera inflado
    LinearLayout amizades, posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nome = (TextView) findViewById(R.id.lbNome);
        profissao = (TextView) findViewById(R.id.lbProfissao);
        estilo = (TextView) findViewById(R.id.lbEstilo);
        totalAmigos = (TextView) findViewById(R.id.lbTotalAmigos);
        totalAvaliacoes = (TextView) findViewById(R.id.lbTotalAvaliacoes);
        imgUsuario = (CircleImageView) findViewById(R.id.imgUsuario);

        amizades = (LinearLayout) findViewById(R.id.linearLayout);
        posts = (LinearLayout) findViewById(R.id.relativeLayout);

        if (getIntent().getIntExtra("usuario", 0) == 0)
            getUsuario(DadosUsuario.codigo);
        else
            getUsuario(getIntent().getIntExtra("usuario", 0));

        super.onResume();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    // seta os dados principais do usuario alvo
    private void getUsuario(final int usu) {

        new CrtlUsuario(this).trazer(usu, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usuario = (Usuario) obj;
                setTotalAvaliacoes();
            }

            @Override
            public void falha() {
                Toast.makeText(PerfilActivity2.this,"Erro ao carregar usuario", Toast.LENGTH_LONG).show();
                setTotalAvaliacoes();
            }
        });

    }

    private void setTotalAvaliacoes() {
        new CrtlAvaliacao(this).contar("usuario = " + usuario.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Resposta resp = (Resposta) obj;
                totalAvaliacoes.setText(String.valueOf(resp.getValor()));
                setTotalAmigos();
            }

            @Override
            public void falha() {
                totalAvaliacoes.setText("0");
                setTotalAmigos();
            }
        });
    }

    private void setTotalAmigos() {

        new CrtlAmigos(this).contar("amigoAdd = " + usuario.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Resposta resp = (Resposta) obj;
                totalAmigos.setText(String.valueOf(resp.getValor()));
                setDadosUsuario();
            }

            @Override
            public void falha() {
                totalAmigos.setText("0");
                setDadosUsuario();
            }
        });

    }

    private void setDadosUsuario(){
        setTitle(usuario.getNome());
        nome.setText(usuario.getNome());
        profissao.setText(usuario.getProfissao());
        estilo.setText(usuario.getAlimentacao());
        new ImageLoaderTask(imgUsuario).execute(Config.caminhoImageTumb + usuario.getImagem());

        getPosts();
    }
    // *****************************************


    // traz os posts que serão inflados na view
    private void getPosts() {

        //"WHERE usuario = " + 1 + " ORDER BY data DESC"
        new CrtlPost(this).listar("ORDER BY data DESC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaPost = new ArrayList<>();
                for(Object obj : lista)
                    listaPost.add((Post) obj);

                addPost();
            }

            @Override
            public void falha() {
                //falha();
            }
        });
    }


    private void addPost(){

//        for(Post p : listaPost) {
//            if (p.getTipo() == 1)
//                new MontaViewComentario(this, posts, p).execute();
//            else if (p.getTipo() == 2)
//                new MontarViewAddAmigo(this, posts, p).execute();
//            else if (p.getTipo() == 3)
//                new MontarViewAvaliacao(this, posts, p).execute();
//        }

    }

    private void falha(){
        ImageView falha = new ImageView(this);
        falha.setImageResource(R.drawable.back_falha_carregar);
        posts.addView(falha);

        falha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                posts.removeAllViews();
                recarregar = (SwipeRefreshLayout) findViewById(R.id.recarregar);
                recarregar.setRefreshing(true);
                getPosts();

            }
        });
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
