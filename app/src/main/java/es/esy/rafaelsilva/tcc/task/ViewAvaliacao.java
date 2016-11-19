package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.AvaliacaoSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.ComentariosAvaliacaoActivity;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlComentarioAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlCurtidaAvaliacao;
import es.esy.rafaelsilva.tcc.controle.CtrlProduto;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.ComentarioAvaliacao;
import es.esy.rafaelsilva.tcc.modelo.CurtidaAvaliacao;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.CompartilharExternamente;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewAvaliacao {
    private AvaliacaoSP sp;
    private CallbackView callback;
    private View v;
    private Context contexto;
    private Post post;
    private Avaliacao av;
    private Usuario usu;
    private Produto prod;
    private boolean existeSP = false;
    private List<ComentarioAvaliacao> comentarios; // ok
    private List<CurtidaAvaliacao> curtidas; // ok

    public ViewAvaliacao(Context contexo, View view, Post post) {
        this.contexto = contexo;
        this.post = post;
        this.v = view;
        this.sp = new AvaliacaoSP(contexto, "TCC_AVALIACAO_"+post.getCodigo());
    }

    public void getView(CallbackView callback) {
        this.callback = callback;

        Post postSP = sp.lerPost();
        if (postSP != null) {
            if (postSP.getEditado() == post.getEditado()) {
                av = sp.lerAvaliacao();
                usu = sp.lerUsuario();
                prod = sp.lerProduto();
                curtidas = sp.lerCurtidas();
                comentarios = sp.lerComentarios();

                existeSP = true;
                montar();
            }else {
                getAvaliacao();
            }
        }else{
            getAvaliacao();
        }
    }

    private void getAvaliacao(){
        new CtrlAvaliacao(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
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
        try {
            new CtrlUsuario(contexto).trazer(av.getUsuario(), new CallbackTrazer() {
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
        }catch (Exception e){
            e.printStackTrace();
            callback.view(null);
        }
    }

    private void getProduto(){
        try {
            new CtrlProduto(contexto).trazer(av.getProduto(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    prod = (Produto) obj;
                    getCurtidas();
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

    private void getCurtidas(){
        new CtrlCurtidaAvaliacao(contexto).listar("WHERE avaliacao = " + av.getCodigo(), new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                curtidas = new ArrayList<>();
                for (Object obj : lista)
                    curtidas.add((CurtidaAvaliacao) obj);

                getComentarios();
            }

            @Override
            public void falha() {
                getComentarios();
            }
        });
    }

    private void getComentarios() {
        new CtrlComentarioAvaliacao(contexto).listar("WHERE avaliacao = "+av.getCodigo(), new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                comentarios = new ArrayList<>();
                for (Object obj : lista)
                    comentarios.add((ComentarioAvaliacao) obj);

                montar();
            }

            @Override
            public void falha() {
                montar();
            }
        });
    }

    private boolean montar(){

        if (!existeSP){
            sp.salvar(post,av,prod,usu,curtidas,comentarios);
        }

        try {
            final TextView nome, data, produto, avaliacao, qtdAddOne, numComent;
            CircleImageView imgUsuario, imgProduto;
            RatingBar estrela;
            final ImageView addOne, coment;

            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            data = (TextView) v.findViewById(R.id.lbData);
            nome.setText(usu.getNome());
            addOne = (ImageView) v.findViewById(R.id.imgAddOne);
            coment = (ImageView) v.findViewById(R.id.imgComentarios);
            qtdAddOne = (TextView) v.findViewById(R.id.lbAddOne);
            numComent = (TextView) v.findViewById(R.id.lbComentarios);

            final boolean[] flag2 = {false};
            int curtido = 0;

            usu.setImagemPerfil(imgUsuario, contexto);

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
                data.setText("avaliou " + Util.formatHoraHHMM(post.getData()));
            else
                data.setText("avaliou em " + Util.formatDataDDmesYYYY(post.getData()));


            // dados da avaliação
            imgProduto = (CircleImageView) v.findViewById(R.id.imgProduto);
            produto = (TextView) v.findViewById(R.id.lbProduto);
            avaliacao = (TextView) v.findViewById(R.id.lbAvaliacao);
            estrela = (RatingBar) v.findViewById(R.id.estrelas);

            prod.setImgIcone(imgProduto, contexto);
            produto.setText(prod.getNome());
            avaliacao.setText(av.getComentario());
            estrela.setRating(av.getEstrelas());


            if (curtidas != null)
                for (CurtidaAvaliacao curtida : curtidas){
                    if (curtida.getUsuario() == DadosUsuario.codigo){
                        addOne.setImageResource(R.drawable.ic_added);
                        flag2[0] = true;
                        curtido = 1;
                    }
                }


            if (curtidas != null) {
                qtdAddOne.setText(String.valueOf(curtidas.size()) + " curtiu");
            }else {
                qtdAddOne.setText("");
            }

            // exclui curtida
            addOne.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (flag2[0]) {
                        addOne.setImageResource(R.drawable.ic_add_one);
                        new CtrlCurtidaAvaliacao(contexto).excluir(av.getCodigo(), av.getPai(), new CallbackExcluir() {
                            @Override
                            public void resultadoExcluir(boolean flag) {
                                if (!flag)
                                    addOne.setImageResource(R.drawable.ic_added);
                            }

                            @Override
                            public void falha() {
                                addOne.setImageResource(R.drawable.ic_added);
                            }
                        });


                        int curtiu = 0;
                        if (curtidas != null)
                            curtiu = curtidas.size() - 1;

                        qtdAddOne.setText(String.valueOf(curtiu) + " curtiu");

                        flag2[0] = false;
                    }

                    return true;

                }
            });

            //add curtida
            final int finalCurtido = curtido;
            addOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!flag2[0]) {

                        addOne.setImageResource(R.drawable.ic_added);
                        new CtrlCurtidaAvaliacao(contexto).curtir(av.getCodigo(), av.getPai(), new CallbackSalvar() {
                            @Override
                            public void resultadoSalvar(Object obj) {
                                Resposta rsp = (Resposta) obj;
                                if (!rsp.isFlag())
                                    addOne.setImageResource(R.drawable.ic_add_one);
                            }

                            @Override
                            public void falha() {
                                addOne.setImageResource(R.drawable.ic_add_one);
                            }
                        });


                        int curtiu = 0;
                        if (curtidas != null)
                            curtiu = curtidas.size() + 1 - finalCurtido;
                        else
                            curtiu = 1;

                        qtdAddOne.setText(String.valueOf(curtiu) + " você curtiu");

                        flag2[0] = true;

                    }

                }
            });

            if (comentarios != null){
                coment.setImageResource(R.drawable.ic_comented);
                numComent.setText(String.valueOf(comentarios.size()) + " comentou");
            }else{
                numComent.setText("");
            }

            coment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(contexto, ComentariosAvaliacaoActivity.class);
                    intent.putExtra("post", av.getCodigo());
                    intent.putExtra("pai", av.getPai());
                    contexto.startActivity(intent);

                }
            });

            new CompartilharExternamente(contexto, v, "Comentario feito App TCC");

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
