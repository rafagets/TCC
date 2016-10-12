package es.esy.rafaelsilva.tcc.task;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.ReputacaoActivity;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 12/10/2016.
 */
public class ReputacaoTask extends AsyncTask<String, Void, Boolean> {

    private SwipeRefreshLayout recarregar;
    private Context contexto;
    private Produto produto;
    private Activity home;

    public ReputacaoTask(Context contexto, SwipeRefreshLayout recarregar, Produto produto) {
        this.contexto = contexto;
        this.home = ((ReputacaoActivity) contexto);
        this.recarregar = recarregar;
        this.produto = produto;
    }

    @Override
    protected Boolean doInBackground(String... values) {

        for (Avaliacao av : produto.getListaAvaliacao()) {
            av.setUsuarioObj(new Usuario().getUsuarioObj(av.getUsuario()));
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        recarregar.setRefreshing(false);

        float soma = 0;
        for (Avaliacao av : produto.getListaAvaliacao())
            soma = soma + av.getEstrelas();
        float media = soma / produto.getListaAvaliacao().size();

        RatingBar estrelas = (RatingBar) home.findViewById(R.id.estrelas);
        estrelas.setRating(media);

        TextView totalAvaliacao = (TextView) home.findViewById(R.id.lbTotalAvaliacoes);
        totalAvaliacao.setText(String.valueOf(produto.getListaAvaliacao().size()) + " avaliação");

        // implementação do historico
        final LinearLayout layout = (LinearLayout) home.findViewById(R.id.container);
        // caso haja alguma view essa é destruida
        layout.removeAllViews();

        if (flag)

            try {

                for (int i = 0; i < produto.getListaAvaliacao().size(); i++) {
                    this.inflarAvaliacao(produto.getListaAvaliacao().get(i), layout);
                }

            } catch (Exception e){

                e.printStackTrace();

                ImageView falha = new ImageView(contexto);
                falha.setImageResource(R.drawable.back_falha_carregar);
                layout.addView(falha);

                falha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        layout.removeAllViews();
                        recarregar = (SwipeRefreshLayout) home.findViewById(R.id.recarregar);
                        recarregar.setRefreshing(true);
                        ((ReputacaoActivity) contexto).loadAvaliacoes();

                    }
                });
            }

        else {

            ImageView falha = new ImageView(contexto);
            falha.setImageResource(R.drawable.back_falha_carregar);
            layout.addView(falha);

            falha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    layout.removeAllViews();
                    recarregar = (SwipeRefreshLayout) home.findViewById(R.id.recarregar);
                    recarregar.setRefreshing(true);
                    ((ReputacaoActivity) contexto).loadAvaliacoes();

                }
            });

        }
    }


    private void inflarAvaliacao(Avaliacao p, LinearLayout layout){
        View v = home.getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);

        final TextView nome, data, prod, avaliacao;
        CircleImageView imgUsuario, imgProduto;
        RatingBar estrela;

        Usuario usu = p.getUsuarioObj();

        imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
        nome = (TextView) v.findViewById(R.id.lbNome);
        data = (TextView) v.findViewById(R.id.lbData);
        nome.setText(usu.getNome());

        ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
        downImg.execute(Config.caminhoImageTumb + usu.getImagem());

        String[] temp = p.getData().split(" ");
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataForm = sdf.format(date);

        if (temp[0].equals(dataForm))
            data.setText("avaliou às " + Util.formatHoraHHMM(p.getData()));
        else
            data.setText("avaliou em " + Util.formatDataDDmesYYYY(p.getData()));


        // dados da avaliação
        imgProduto = (CircleImageView) v.findViewById(R.id.imgProduto);
        prod = (TextView) v.findViewById(R.id.lbProduto);
        avaliacao = (TextView) v.findViewById(R.id.lbAvaliacao);
        estrela = (RatingBar) v.findViewById(R.id.estrelas);

        imgProduto.setImageBitmap( ((ReputacaoActivity) contexto).icon);

        prod.setText(produto.getNome());
        avaliacao.setText(p.getComentario());
        estrela.setRating(p.getEstrelas());

        layout.addView(v);
    }


}
