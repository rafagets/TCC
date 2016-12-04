package es.esy.rafaelsilva.tcc.views;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.CompraSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.DetalhesProdutoActivity;
import es.esy.rafaelsilva.tcc.activity.Mapa_Activity;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlCompra;
import es.esy.rafaelsilva.tcc.controle.CtrlMercado;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlProdutor;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Compra;
import es.esy.rafaelsilva.tcc.modelo.Mercado;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Produtor;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.CompartilharExternamente;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Criado por Rafael em 17/11/2016, enjoy it.
 */
public class ViewCompra {
    private CompraSP sp;
    private CallbackView callback;
    private View view;
    private Context contexto;
    private Post post;
    private Usuario u; // ok
    private Compra compra; // ok
    private Mercado mercado;
    private Produto produto;
    private Produtor produtor;
    private boolean existeSP = false;

    public ViewCompra(Context contexto, View view, Post post) {
        this.contexto = contexto;
        this.view = view;
        this.post = post;
        this.sp = new CompraSP(contexto, "TCC_COMPRA_"+post.getCodigo());
    }

    public void getView(CallbackView callback){
        this.callback = callback;

        Post postSP = sp.lerPost();
        if (postSP != null) {
            if (postSP.getEditado() == post.getEditado()) {
                compra = sp.lerCompra();
                u = sp.lerUsuario();
                mercado = sp.lerMercado();
                produto = sp.lerProduto();
                produtor = sp.lerProdutor();

                existeSP = true;
                montar();
            }else {
                getCompra();
            }
        }else{
            getCompra();
        }
    }

    private void getCompra() {
        new CtrlCompra(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                compra = (Compra) obj;
                getUsuario();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }

    private void getUsuario(){
        try {
            new CtrlUsuario(contexto).trazer(compra.getUsuario(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    u = (Usuario) obj;
                    getProduto();
                }

                @Override
                public void falha() {
                    callback.view(null);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            callback.view(null);
        }

    }

    private void getProduto(){
        try {
            new CtrlProduto(contexto).trazer(compra.getProduto(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    produto = (Produto) obj;
                    getProdutor();
                }

                @Override
                public void falha() {
                    callback.view(null);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            callback.view(null);
        }
    }

    private void getProdutor(){
        new CtrlProdutor(contexto).trazer(produto.getProdutor(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                produtor = (Produtor) obj;
                getMercado();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }

    private void getMercado(){
        new CtrlMercado(contexto).trazer(1, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                mercado = (Mercado) obj;
                montar();
            }

            @Override
            public void falha() {
                callback.view(null);
            }
        });
    }


    private void montar() {
        if (!existeSP){
            sp.salvar(compra,post,u,mercado,produto, produtor);
        }

        try {
            final TextView nome, data, comentario, nomeProduto, nomeMercado, nomeProdutor;
            CircleImageView imgUsuario, imgproduto;
            ImageView verMapa;

            imgUsuario = (CircleImageView) view.findViewById(R.id.imgUsuario);
            nome = (TextView) view.findViewById(R.id.lbNome);
            data = (TextView) view.findViewById(R.id.lbData);
            comentario = (TextView) view.findViewById(R.id.lbComentario);
            nomeProduto = (TextView) view.findViewById(R.id.txtNomeProduto);
            nomeMercado = (TextView) view.findViewById(R.id.txtMercado);
            nomeProdutor = (TextView) view.findViewById(R.id.txtNomeProdutor);
            imgproduto = (CircleImageView) view.findViewById(R.id.imgProduto);
            verMapa = (ImageView) view.findViewById(R.id.verMapa);

            u.setImagemPerfil(imgUsuario, contexto);
            produto.setImgIcone(imgproduto, contexto);

            nomeProduto.setText(produto.getNome());
            nomeMercado.setText(mercado.getNome());
            nomeProdutor.setText(produtor.getNome());
            comentario.setText(compra.getComentario());

            imgproduto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, DetalhesProdutoActivity.class);
                    intent.putExtra("produto", produto.getCodigo());
                    contexto.startActivity(intent);
                }
            });

            imgUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, PerfilActivity.class);
                    intent.putExtra("usuario", u.getCodigo());
                    contexto.startActivity(intent);
                }
            });

            verMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, Mapa_Activity.class);
                    intent.putExtra("cordenadas", mercado.getCordenadas());
                    intent.putExtra("nome", produto.getNome());
                    intent.putExtra("data", mercado.getNome());
                    contexto.startActivity(intent);
                }
            });

            nome.setText(u.getNome());

            String[] temp = compra.getData().split(" ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataForm = sdf.format(date);

            if (temp[0].equals(dataForm)) {
                temp = temp[1].split(":");

                String txt;
                if (compra.getNotificacao() == 0)
                    txt = "compartilhou um produto às ";
                else {
                    txt = "levou pra casa às ";
                }
                data.setText(txt + temp[0] + ":" + temp[1]);
            }else{
                String txt;
                if (compra.getNotificacao() == 0)
                    txt = "compartilhou um produto em ";
                else {
                    txt = "levou pra casa em ";
                }
                temp = temp[0].split("-");
                data.setText(txt + Util.formatDataDDmesYYYY(compra.getData()));
            }

            new CompartilharExternamente(contexto, view, "Comentario feito App TCC");

            callback.view(view);
            Log.i("*** OK","View de COMPRA do Post ["+this.post.getCodigo()+"] adicionada.");

        }catch (Exception e){
            Log.e("*** ERRO", "View de COMPRA não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }

    }
}
