package es.esy.rafaelsilva.tcc.views;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.ComentarioSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.ComentariosPostActivity;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlComentario;
import es.esy.rafaelsilva.tcc.controle.CtrlComentarioPost;
import es.esy.rafaelsilva.tcc.controle.CtrlCurtidaComentario;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.controle.CtrlCurtidaComentario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.CompartilharExternamente;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewComentario {
    private ComentarioSP sp;
    private CallbackView callback;
    private View view;
    private Context contexto;
    private Post post;
    private Comentario c;
    private Usuario u; // ok
    private List<ComentarioPost> cp; // ok
    private List<CurtidaComentario> cc; // ok
    private boolean existeSP = false;

    public ViewComentario(Context contexto, View view, Post post) {
        this.contexto = contexto;
        this.view = view;
        this.post = post;
        this.sp = new ComentarioSP(contexto, "TCC_COMENTARIO_"+post.getCodigo());
    }

    public void getView(CallbackView callback){
        this.callback = callback;

        Post postSP = sp.lerPost();
        if (postSP != null) {
            if (postSP.getEditado() == post.getEditado()) {
                c = sp.lerComentario();
                u = sp.lerUsuario();
                cp = sp.lerCP();
                cc = sp.lerCC();

                existeSP = true;
                montar();
            }else {
                getComentario();
            }
        }else{
            getComentario();
        }
    }

    private void getComentario(){
        new CtrlComentario(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                c = (Comentario) obj;
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
            new CtrlUsuario(contexto).trazer(c.getUsuarioPost(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    u = (Usuario) obj;
                    getComentarioPost();
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

    private void getComentarioPost(){
        new CtrlComentarioPost(contexto).listar("WHERE coment = " + c.getCodigo(), new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                cp = new ArrayList<>();
                for (Object obj : lista)
                    cp.add((ComentarioPost) obj);
                getCurtidasComentarios();
            }

            @Override
            public void falha() {
                getCurtidasComentarios();
            }
        });
    }

    private void getCurtidasComentarios(){
        new CtrlCurtidaComentario(contexto).listar("WHERE comentario = " + c.getCodigo(), new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                cc = new ArrayList<>();
                for (Object obj : lista) {
                    cc.add((CurtidaComentario) obj);
                }
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
            sp.salvar(cc,c,cp,post,u);
        }

        try {
            final TextView nome, post, data, qtdAddOne, numComent;
            CircleImageView imgUsuario;
            final ImageView addOne, coment;

            imgUsuario = (CircleImageView) view.findViewById(R.id.imgUsuario);
            nome = (TextView) view.findViewById(R.id.lbNome);
            post = (TextView) view.findViewById(R.id.lbPost);
            data = (TextView) view.findViewById(R.id.lbData);
            addOne = (ImageView) view.findViewById(R.id.imgAddOne);
            coment = (ImageView) view.findViewById(R.id.imgComentarios);
            qtdAddOne = (TextView) view.findViewById(R.id.lbAddOne);
            numComent = (TextView) view.findViewById(R.id.lbComentarios);

            final boolean[] flag2 = {false};
            int curtido = 0;

            if (u.getImagem() != null)
            u.setImagemPerfil(imgUsuario, contexto);

            imgUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, PerfilActivity.class);
                    intent.putExtra("usuario", u.getCodigo());
                    contexto.startActivity(intent);
                }
            });

            nome.setText(u.getNome());
            post.setText(c.getComentario());

            String[] temp = c.getData().split(" ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataForm = sdf.format(date);

            if (temp[0].equals(dataForm)) {
                temp = temp[1].split(":");
                data.setText("comentou às " + temp[0] + ":" + temp[1]);
            }else{
                temp = temp[0].split("-");
                data.setText("comentou em " + Util.formatDataDDmesYYYY(c.getData()));
            }

            if (cc != null)
                for (CurtidaComentario curtida : cc){
                    if (curtida.getUsuario() == DadosUsuario.codigo){
                        addOne.setImageResource(R.drawable.ic_added);
                        flag2[0] = true;
                        curtido = 1;
                    }
                }


            if (cc != null) {
                qtdAddOne.setText(String.valueOf(cc.size()) + " curtiu");
            }else {
                qtdAddOne.setText("");
            }

            // exclui curtida
            addOne.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (flag2[0]) {
                        addOne.setImageResource(R.drawable.ic_add_one);
                        new CtrlCurtidaComentario(contexto).excluir(c.getCodigo(), new CallbackSalvar() {
                            @Override
                            public void resultadoSalvar(Object obj) {
                                Resposta rsp = (Resposta) obj;
                                if (!rsp.isFlag())
                                    addOne.setImageResource(R.drawable.ic_added);
                            }

                            @Override
                            public void falha() {
                                addOne.setImageResource(R.drawable.ic_added);
                            }
                        });


                        int curtiu = 0;
                        if (cc != null)
                            curtiu = cc.size() - 1;

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
                        new CtrlCurtidaComentario(contexto).curtir(c.getCodigo(), c.getPai(), new CallbackSalvar() {
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
                        if (cc != null)
                            curtiu = cc.size() + 1 - finalCurtido;
                        else
                            curtiu = 1;

                        qtdAddOne.setText(String.valueOf(curtiu) + " você curtiu");

                        flag2[0] = true;

                    }

                }
            });



            if (cp != null){
                coment.setImageResource(R.drawable.ic_comented);
                numComent.setText(String.valueOf(cp.size()) + " comentou");
            }else{
                numComent.setText("");
            }


            coment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(contexto, ComentariosPostActivity.class);
                    intent.putExtra("post", c.getCodigo());
                    intent.putExtra("pai", c.getPai());
                    contexto.startActivity(intent);

                }
            });

            new CompartilharExternamente(contexto, view, "Comentario feito App TCC");

            callback.view(view);
            Log.i("*** OK","View de COMENTÁRIO do Post ["+this.post.getCodigo()+"] adicionada.");

        }catch (Exception e){
            Log.e("*** ERRO", "View de COMENTÁRIO não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }

        return false;
    }

}
