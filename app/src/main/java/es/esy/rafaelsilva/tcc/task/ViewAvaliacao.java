package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CrtlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CrtlProduto;
import es.esy.rafaelsilva.tcc.controle.CrtlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewAvaliacao {
    private CallbackView callback;
    private View v;
    private Context contexto;
    private Post post;
    private Avaliacao av;
    private Usuario usu;
    private Produto prod;
//    private List<ComentarioPost> cp; // ok
//    private List<CurtidaComentario> cc; // ok

    public ViewAvaliacao(Context contexo, View view, Post post) {
        this.contexto = contexo;
        this.post = post;
        this.v = view;
    }

    public void getView(CallbackView callback) {
        this.callback = callback;
        getAvaliacao();
    }

    private void getAvaliacao(){
        new CrtlAvaliacao(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                av = (Avaliacao) obj;
                getUsuario();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }

    private void getUsuario(){
        new CrtlUsuario(contexto).trazer(av.getUsuario(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usu = (Usuario) obj;
                getProduto();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }

    private void getProduto(){
        new CrtlProduto(contexto).trazer(av.getProduto(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                prod = (Produto) obj;
                montar();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }

    private boolean montar(){
        try {
            final TextView nome, data, produto, avaliacao;
            CircleImageView imgUsuario, imgProduto;
            RatingBar estrela;

            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            data = (TextView) v.findViewById(R.id.lbData);
            nome.setText(usu.getNome());

            if (usu.getImagem() != null) {
                ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
                downImg.execute(Config.caminhoImageTumb + usu.getImagem());
            }else{
                imgUsuario.setImageResource(R.drawable.ic_usuario);
            }

            imgUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, PerfilActivity.class);
                    intent.putExtra("usuario", usu.getCodigo());
                    contexto.startActivity(intent);
                }
            });

            String[] temp = post.getData().split(" ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataForm = sdf.format(date);

            if (temp[0].equals(dataForm))
                data.setText("avaliou às " + Util.formatHoraHHMM(post.getData()));
            else
                data.setText("avaliou em " + Util.formatDataDDmesYYYY(post.getData()));


            // dados da avaliação
            imgProduto = (CircleImageView) v.findViewById(R.id.imgProduto);
            produto = (TextView) v.findViewById(R.id.lbProduto);
            avaliacao = (TextView) v.findViewById(R.id.lbAvaliacao);
            estrela = (RatingBar) v.findViewById(R.id.estrelas);

            ImageLoaderTask downImg2 = new ImageLoaderTask(imgProduto);
            downImg2.execute(Config.caminhoImageProdutos + prod.getImgicone());

            produto.setText(prod.getNome());
            avaliacao.setText(av.getComentario());
            estrela.setRating(av.getEstrelas());

            callback.view(v);
            Log.i("*** OK","View de AVALIAÇÃO do Post ["+this.post.getCodigo()+"] adicionada.");

        } catch (Exception e) {
            Log.e("*** ERRO", "View de AVALIAÇÃO não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }
        return false;
    }
}
