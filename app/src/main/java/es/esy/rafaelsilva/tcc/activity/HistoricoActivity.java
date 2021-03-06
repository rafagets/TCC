package es.esy.rafaelsilva.tcc.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlCompra;
import es.esy.rafaelsilva.tcc.controle.CtrlHistorico;
import es.esy.rafaelsilva.tcc.controle.CtrlLote;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlProdutor;
import es.esy.rafaelsilva.tcc.controle.CtrlTipo;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Produtor;
import es.esy.rafaelsilva.tcc.modelo.Tipo;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

public class HistoricoActivity extends AppCompatActivity {

    private List<Historico> listaHistorico;
    private Context contexto = this;
    private ProgressBar bar;
    private FloatingActionButton fab;
    private Lote lote;
    public Produto produto;
    private List<Avaliacao> listaAvaliacao;
    private Usuario usuario;
    private Produtor produtor;
    private CircleImageView imgIconeProduto;
    private ImageView imgHeaderProduto;
    private LinearLayout layoutEstrelas;

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

        layoutEstrelas = (LinearLayout) findViewById(R.id.layoutEstrelas);
        imgIconeProduto = (CircleImageView) findViewById(R.id.imgIcone);
        imgHeaderProduto = (ImageView) findViewById(R.id.imgFundo);
        this.monitorarCliqueImagemHeader();
        this.monitorarCliqueImagemIcone();

        bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);


        final String l = String.valueOf(getIntent().getStringExtra("lote"));
        Log.e("+++++++++++++++", l);

        trazerLote(l);
        getUsuario();

        ImageView btSaude = (ImageView) findViewById(R.id.btSaude);
        btSaude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoricoActivity.this, DetalhesProdutoActivity.class);
                intent.putExtra("produto", produto.getCodigo());
                startActivity(intent);
            }
        });

        ImageView btLocalizacao = (ImageView) findViewById(R.id.btLocalizacao) ;
        btLocalizacao.setOnClickListener(verMapa());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comprar();
            }
        });
    }

    private void monitorarCliqueImagemIcone(){
        imgIconeProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(HistoricoActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                CircleImageView imgUsuarioFull = new CircleImageView(HistoricoActivity.this);
                produto.setImgIcone(imgUsuarioFull, HistoricoActivity.this);
                mensagem.setView(imgUsuarioFull);
                mensagem.show();
            }
        });
    }

    private void monitorarCliqueImagemHeader(){
        imgHeaderProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(HistoricoActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                ImageView imgUsuarioFull = new ImageView(HistoricoActivity.this);
                produto.setImgFundo(imgUsuarioFull, HistoricoActivity.this);
                mensagem.setView(imgUsuarioFull);
                mensagem.show();
            }
        });
    }

    private void trazerLote(String l) {
        new CtrlLote(this).trazer(Integer.parseInt(l), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                lote = (Lote) obj;
                getProd();
                getAvaliacoes();
                getHistorico();
            }

            @Override
            public void falha() {
                bar.setVisibility(View.GONE);
                Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
                finish();
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

    private void getProdutor(){
        new CtrlProdutor(this).trazer(produto.getProdutor(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                produtor = (Produtor) obj;
            }

            @Override
            public void falha() {

            }
        });
    }

    private View.OnClickListener verMapa() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(HistoricoActivity.this, Mapa_Activity.class);
                intent.putExtra("historico", gson.toJson(listaHistorico));
                startActivity(intent);
            }
        };
    }


    private void comprar() {
        AlertDialog.Builder mensagem = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.inflater_dialog_compra, null);

        CircleImageView imgUsuario = (CircleImageView) view.findViewById(R.id.imgUsuario);
        CircleImageView imgProduto = (CircleImageView) view.findViewById(R.id.imgProduto);
        TextView nomeUsuario = (TextView) view.findViewById(R.id.txtNomeUsuario);
        TextView nomeProduto = (TextView) view.findViewById(R.id.txtNomeProduto);
        TextView nomeProdutor = (TextView) view.findViewById(R.id.txtNomeProdutor);
        TextView dataValidade = (TextView) view.findViewById(R.id.txtDataValidade);
        final EditText post = (EditText) view.findViewById(R.id.txtPost);
        final Spinner spinner = (Spinner) view.findViewById(R.id.status);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);

        String[] status = new String[] {"Público","Amigos","Privado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.estilo_spinner, status);
        spinner.setAdapter(adapter);

        nomeUsuario.setText(usuario.getNome());
        usuario.setImagemPerfil(imgUsuario, this);
        produto.setImgIcone(imgProduto, this);
        nomeProduto.setText(produto.getNome());
        nomeProdutor.setText(produtor.getNome());
        dataValidade.setText("Validade: "+Util.formatDataDDmesYYYY(lote.getDatavencimento()));

        mensagem.setView(view);

        mensagem.setPositiveButton("Publicar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoge, int which) {
                Toast.makeText(HistoricoActivity.this, "Ok.", Toast.LENGTH_LONG).show();

                int finalCarater = spinner.getSelectedItemPosition() + 1;
                int check = 0;
                if (checkBox.isChecked())
                    check=1;

                estocar(finalCarater, check, post.getText().toString());
            }
        });

        mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(HistoricoActivity.this, "Ação cancelada.", Toast.LENGTH_LONG).show();

            }
        });

        mensagem.show();
    }

    private void estocar(int carater,int notificacao, String post) {
        new CtrlCompra(this).salvar(carater, notificacao, produto, lote, post, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.isFlag()){
                    fab.setVisibility(View.GONE);
                    Toast.makeText(HistoricoActivity.this, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(HistoricoActivity.this, "Falha\nProduto não foi postado no seu mural.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void falha() {
                Toast.makeText(HistoricoActivity.this, "Falha\nProduto não foi postado no seu mural.", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void getProd(){
        new CtrlProduto(this).trazer(lote.getProduto(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                produto = (Produto) obj;
                setTitle(produto.getNome());
                getProdutor();

                ImageView imgShare = (ImageView) findViewById(R.id.imgShare);

                imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, produto.getNome());
                        sendIntent.setType("text/plain");
                        contexto.startActivity(sendIntent);
                    }
                });
            }

            @Override
            public void falha() {
                bar.setVisibility(View.GONE);
                Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    public void getAvaliacoes(){
        new CtrlAvaliacao(this).listar("WHERE produto = "+lote.getProduto(), new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaAvaliacao = new ArrayList<>();
                for (Object obj : lista)
                    listaAvaliacao.add((Avaliacao) obj);

                float soma = 0;
                for (Avaliacao av : listaAvaliacao)
                    soma = soma + av.getEstrelas();
                float media = soma / lista.size();

                RatingBar estrelas = (RatingBar) findViewById(R.id.estrelas);
                estrelas.setRating(media);

                TextView totalAvaliacao = (TextView) findViewById(R.id.lbTotalAvaliacoes);
                totalAvaliacao.setText(String.valueOf(lista.size()) + " avaliação");
            }

            @Override
            public void falha() {
                bar.setVisibility(View.GONE);
                //Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
                //finish();
            }
        });
    }

    public void getHistorico(){
        new CtrlHistorico(this).listar("WHERE lote = "+lote.getCodigo()+" ORDER BY data ASC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaHistorico = new ArrayList<>();
                for (Object obj : lista)
                    listaHistorico.add((Historico) obj);

                getTipo(0);
            }

            @Override
            public void falha() {

            }
        });
    }

    public void getTipo(final int posicao){
        if (posicao <= listaHistorico.size() - 1) {
            new CtrlTipo(this).trazer(listaHistorico.get(posicao).getTipo(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    listaHistorico.get(posicao).setTipoObj((Tipo) obj);
                    getTipo(posicao + 1);

                }

                @Override
                public void falha() {

                }
            });
        }else{
            montarHistorico();
        }
    }

    public void montarHistorico(){
        final ImageView fundo = (ImageView) findViewById(R.id.imgFundo);
        final CircleImageView icone = (CircleImageView) findViewById(R.id.imgIcone);
        final TextView prod, produtor, numAvaliacoes;
        RatingBar estrelas = (RatingBar) findViewById(R.id.estrelas);

        if (listaAvaliacao != null) {
            numAvaliacoes = (TextView) findViewById(R.id.lbTotalAvaliacoes);
            numAvaliacoes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, ReputacaoActivity.class);
                    icone.buildDrawingCache();
                    intent.putExtra("icone", icone.getDrawingCache());
                    intent.putExtra("estrelas", (float) 2.5);
                    intent.putExtra("nome", produto.getNome());
                    intent.putExtra("produto", produto.getCodigo());
                    produto.setListaAvaliacao(listaAvaliacao);
                    intent.putExtra("obj", produto);
                    contexto.startActivity(intent);
                }
            });

            monitorarCliqueEstrela(icone);
        }

        prod = (TextView) findViewById(R.id.lbProduto);
        prod.setText(produto.getNome());

        produtor = (TextView) findViewById(R.id.lbProdutor);
        produtor.setText(this.produtor.getNome());

        produto.setImgIcone(icone, this);
        produto.setImgFundo(fundo, this);

        // implementação do historico
        RelativeLayout view = (RelativeLayout) findViewById(R.id.view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.relativeLayout);;
        //for (Historico h : lista){
        for (int i = 0; i < listaHistorico.size(); i++){
            final Historico h = listaHistorico.get(i);
            View v = getLayoutInflater().inflate(R.layout.inflater_historico, null);

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

            h.getTipoObj().setImagem(imgHistorico, this);

            imgHistorico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HistoricoActivity.this, Mapa_Activity.class);
                    intent.putExtra("cordenadas", h.getCordenadas());
                    intent.putExtra("nome", h.getNome());
                    intent.putExtra("data", h.getData());
                    startActivity(intent);
                }
            });

            layout.addView(v);

            if (listaHistorico.size() == i+1)
                fim.setVisibility(View.VISIBLE);
        }

        bar.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
    }

    private void monitorarCliqueEstrela(final CircleImageView icone) {
        layoutEstrelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, ReputacaoActivity.class);
                icone.buildDrawingCache();
                intent.putExtra("icone", icone.getDrawingCache());
                intent.putExtra("estrelas", (float) 2.5);
                intent.putExtra("nome", produto.getNome());
                intent.putExtra("produto", produto.getCodigo());
                produto.setListaAvaliacao(listaAvaliacao);
                intent.putExtra("obj", produto);
                contexto.startActivity(intent);
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
