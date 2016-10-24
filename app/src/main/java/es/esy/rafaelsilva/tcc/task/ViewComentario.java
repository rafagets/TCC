package es.esy.rafaelsilva.tcc.task;

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
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.ComentariosPostActivity;
import es.esy.rafaelsilva.tcc.controle.CrtlComentario;
import es.esy.rafaelsilva.tcc.controle.CrtlComentarioPost;
import es.esy.rafaelsilva.tcc.controle.CrtlCurtidaComentario;
import es.esy.rafaelsilva.tcc.controle.CrtlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewComentario {
    private CallbackView callback;
    private View view;
    private Context contexto;
    private Post post;
    private Comentario c;
    private Usuario u; // ok
    private List<ComentarioPost> cp; // ok
    private List<CurtidaComentario> cc; // ok
    private boolean[] flag = { true, true, true, true };

    public ViewComentario(Context contexto, View view, Post post) {
        this.contexto = contexto;
        this.view = view;
        this.post = post;
    }

    public void getView(CallbackView callback){
        this.callback = callback;
        getComentario();
    }

    private void getComentario(){
        new CrtlComentario(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
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
        new CrtlUsuario(contexto).trazer(c.getUsuarioPost(), new CallbackTrazer() {
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
    }

    private void getComentarioPost(){
        new CrtlComentarioPost(contexto).listar("WHERE coment = " + c.getCodigo(), new CallbackListar() {
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
        new CrtlCurtidaComentario(contexto).listar("WHERE comentario = " + c.getCodigo(), new CallbackListar() {
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

            ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
            downImg.execute(Config.caminhoImageTumb + u.getImagem());

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
                        new CrtlCurtidaComentario(contexto).excluir(c.getCodigo());
                        addOne.setImageResource(R.drawable.ic_add_one);

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
                        new CrtlCurtidaComentario(contexto).curtir(c.getCodigo());
                        addOne.setImageResource(R.drawable.ic_added);

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
                    contexto.startActivity(intent);

                }
            });

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
