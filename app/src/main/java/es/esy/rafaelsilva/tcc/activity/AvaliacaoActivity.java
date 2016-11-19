package es.esy.rafaelsilva.tcc.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlCompra;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Compra;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class AvaliacaoActivity extends AppCompatActivity {
    private CircleImageView imgUsuario, imgProduto;
    private TextView nomeUsuario, nomeProduto;
    private EditText texto;
    private RatingBar estrelas;
    private Spinner status;
    private ProgressBar bar;
    private RelativeLayout layout;
    private FloatingActionButton fab;

    private Compra compra;
    private Produto produto;
    private Usuario usuario;

    private int codigo, pai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Avaliação");

        imgUsuario = (CircleImageView) findViewById(R.id.imgUsuario);
        imgProduto = (CircleImageView) findViewById(R.id.imgProduto);
        nomeUsuario = (TextView) findViewById(R.id.txtNomeUsuario);
        nomeProduto = (TextView) findViewById(R.id.lbProduto);
        texto = (EditText) findViewById(R.id.txtPost);
        estrelas = (RatingBar) findViewById(R.id.estrelas);
        status = (Spinner) findViewById(R.id.status);

        bar = (ProgressBar) findViewById(R.id.progressBar);
        layout = (RelativeLayout) findViewById(R.id.layout);

        bar.setVisibility(View.VISIBLE);
        layout.setVisibility(View.INVISIBLE);

        codigo = getIntent().getIntExtra("codigo", 0);
        pai = getIntent().getIntExtra("pai", 0);

        String[] s = new String[] {"Público","Amigos","Privado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.estilo_spinner, s);
        status.setAdapter(adapter);

        getCompra();
        getUsuario();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (texto.getText().equals("")){
                    Toast.makeText(AvaliacaoActivity.this, "Diga algo sobre o produto", Toast.LENGTH_LONG).show();
                }else{
                    AlertDialog.Builder mensagem = new AlertDialog.Builder(AvaliacaoActivity.this);
                    mensagem.setTitle("Confirme as estrelas");
                    mensagem.setMessage("Você deu \""+estrelas.getRating()+"\" estrelas \nÉ isso mesmo que deseja?");

                    mensagem.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            salvarAvaliacao();
                        }
                    });

                    mensagem.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    mensagem.show();
                }
            }
        });
    }

    private void salvarAvaliacao() {
        int st = status.getSelectedItemPosition() + 1;

        new CtrlAvaliacao(this).postar(st, estrelas.getRating(), texto.getText().toString(), produto.getCodigo(), new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Intent intent = new Intent(AvaliacaoActivity.this, MainActivity.class);
                startActivity(intent);

                Toast.makeText(AvaliacaoActivity.this, "Obridado por sua avaliação. \uD83D\uDC4D", Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void falha() {
                Toast.makeText(AvaliacaoActivity.this, "Ocorreu uma falha \n Tente novamnete", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getUsuario(){
        new CtrlUsuario(this).trazer(DadosUsuario.codigo, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usuario = (Usuario) obj;
            }

            @Override
            public void falha() {

            }
        });
    }

    private void getCompra(){
        new CtrlCompra(this).trazer(pai, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                compra = (Compra) obj;
                getProduto();
            }

            @Override
            public void falha() {

            }
        });
    }

    private void getProduto() {
        new CtrlProduto(this).trazer(compra.getProduto(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                produto = (Produto) obj;
                setarDados();
            }

            @Override
            public void falha() {
                Toast.makeText(AvaliacaoActivity.this, "Falha ao trazer dados importantes.", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            }
        });
    }

    private void setarDados(){
        usuario.setImagemPerfil(imgUsuario, this);
        produto.setImgIcone(imgProduto, this);

        nomeUsuario.setText(usuario.getNome());
        nomeProduto.setText(produto.getNome());

        layout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
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
