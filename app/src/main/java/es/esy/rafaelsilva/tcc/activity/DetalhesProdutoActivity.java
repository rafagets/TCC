package es.esy.rafaelsilva.tcc.activity;

import android.media.Rating;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
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
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Detalhes;
import es.esy.rafaelsilva.tcc.modelo.Produto;

/**
 * Criado por Rafael em 15/11/2016, enjoy it.
 */
public class DetalhesProdutoActivity extends AppCompatActivity {
    CircleImageView imgProd;
    RatingBar aval;
    TextView ttAval, infNut;
    Produto produto;
    List<Avaliacao> listaAvaliacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detales_produto);
        imgProd = (CircleImageView) findViewById(R.id.imgIcone);
        aval = (RatingBar) findViewById(R.id.ratingBar);
        ttAval = (TextView) findViewById(R.id.lbTotalAvaliacoes);
        infNut = (TextView) findViewById(R.id.lbDescricao);
        if (getIntent().getIntExtra("produto",0) > 0){
            selectProduto(getIntent().getIntExtra("produto",0));
            getAvaliacoes(getIntent().getIntExtra("produto",0));
            getInfNutricionais(getIntent().getIntExtra("produto",0));
        }else{
            setTitle("Nenhum Produto Selecionado");
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    private void selectProduto(int codProd) {
        new CtrlProduto(this).trazer(codProd, new CallbackTrazer() {

            @Override
            public void resultadoTrazer(Object obj) {

                produto = (Produto) obj;
                setTitle(produto.getNome());
                produto.setImgIcone(imgProd,DetalhesProdutoActivity.this);


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
               infNut.setText(detalhes.getTabNutricional());
            }

            @Override
            public void falha() {

            }
        });

    }
}