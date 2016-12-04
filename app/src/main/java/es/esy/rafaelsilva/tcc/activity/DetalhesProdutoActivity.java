package es.esy.rafaelsilva.tcc.activity;

import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.PesquisaProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlInfDetails;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlProdutor;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Detalhes;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Produtor;

/**
 * Criado por Rafael em 15/11/2016, enjoy it.
 */
public class DetalhesProdutoActivity extends AppCompatActivity {
    CircleImageView imgProd;
    RatingBar aval;
    TextView ttAval, infNut, nomeProdutor, detalheProdutor;
    Produto produto;
    Produtor produtor;
    List<Avaliacao> listaAvaliacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detales_produto);


        imgProd = (CircleImageView) findViewById(R.id.imgIcone);
        aval = (RatingBar) findViewById(R.id.ratingBar);
        ttAval = (TextView) findViewById(R.id.lbTotalAvaliacoes);
        infNut = (TextView) findViewById(R.id.lbDescricao);
        nomeProdutor = (TextView) findViewById(R.id.lbNomeProdutor);
        detalheProdutor = (TextView) findViewById(R.id.detalhesProdutor);

        this.monitorarCliqueImagemIcone();

        if (getIntent().getIntExtra("produto",0) > 0){
            selectProduto(getIntent().getIntExtra("produto",0));
            getAvaliacoes(getIntent().getIntExtra("produto",0));
            getInfNutricionais(getIntent().getIntExtra("produto",0));
        }else{
            setTitle("Nenhum Produto Selecionado");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void monitorarCliqueImagemIcone(){
        imgProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder mensagem = new AlertDialog.Builder(DetalhesProdutoActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                CircleImageView imgUsuarioFull = new CircleImageView(DetalhesProdutoActivity.this);
                produto.setImgIcone(imgUsuarioFull, DetalhesProdutoActivity.this);
                mensagem.setView(imgUsuarioFull);
                mensagem.show();
            }
        });
    }

    private void selectProduto(int codProd) {
        new CtrlProduto(this).trazer(codProd, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                produto = (Produto) obj;
                setTitle(produto.getNome());
                produto.setImgIcone(imgProd,DetalhesProdutoActivity.this);
                getProdutor();
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

                nomeProdutor.setText(produtor.getNome());
                detalheProdutor.setText(produtor.getDescricao());
            }

            @Override
            public void falha() {

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


    public void getAvaliacoes(int codProd){
        new CtrlAvaliacao(this).listar("WHERE produto = " + codProd, new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                listaAvaliacao = new ArrayList<>();
                for (Object obj : lista)
                    listaAvaliacao.add((Avaliacao) obj);

                float soma = 0;
                for (Avaliacao av : listaAvaliacao)
                    soma = soma + av.getEstrelas();
                float media = soma / lista.size();


                aval.setRating(media);

                if(lista.size() > 1){
                    ttAval.setText(String.valueOf(lista.size()) + " Avaliações");
                }else{
                    ttAval.setText(String.valueOf(lista.size()) + " Avaliação");
                }
            }
            @Override
            public void falha() {
//                bar.setVisibility(View.GONE);
//                Toast.makeText(contexto, "Falha ao carregar", Toast.LENGTH_LONG).show();
//                finish();
            }
        });
    }

    private void getInfNutricionais(int produto) {
        new CtrlInfDetails(this).trazer(produto, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Detalhes detalhes;
                detalhes = (Detalhes) obj;
                try {
                    infNut.setText(detalhes.getTabNutricional());
                }catch (Exception e){

                }
            }

            @Override
            public void falha() {

            }
        });

    }
}