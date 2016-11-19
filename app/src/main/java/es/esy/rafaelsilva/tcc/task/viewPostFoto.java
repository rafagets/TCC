package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.PostFotoSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlGaleriaImgUsuario;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.GaleriaImgUsuario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.CompartilharExternamente;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Criado por Rafael em 15/11/2016, enjoy it.
 */
public class ViewPostFoto {
    private PostFotoSP sp;
    private CallbackView callback;
    private View view;
    private Context contexto;
    private Post post;
    private Usuario u; // ok
    private GaleriaImgUsuario galeria; // ok
    private boolean existeSP = false;

    public ViewPostFoto(Context contexto, View view, Post post) {
        this.contexto = contexto;
        this.view = view;
        this.post = post;
        this.sp = new PostFotoSP(contexto, "TCC_POST_FOTO_"+post.getCodigo());
    }

    public void getView(CallbackView callback){
        this.callback = callback;

        Post postSP = sp.lerPost();
        if (postSP != null) {
            if (postSP.getEditado() == post.getEditado()) {
                galeria = sp.lerGaleria();
                u = sp.lerUsuario();

                existeSP = true;
                montar();
            }else {
                getGaleria();
            }
        }else{
            getGaleria();
        }
    }

    private void getGaleria() {
        new CtrlGaleriaImgUsuario(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                galeria = (GaleriaImgUsuario) obj;
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
            new CtrlUsuario(contexto).trazer(galeria.getUsuario(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    u = (Usuario) obj;
                    montar();
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

    private void montar() {
        if (!existeSP){
            sp.salvar(galeria,post,u);
        }

        try {
            final TextView nome, post, data;
            CircleImageView imgUsuario;
            final ImageView imgPost;

            imgUsuario = (CircleImageView) view.findViewById(R.id.imgUsuario);
            nome = (TextView) view.findViewById(R.id.lbNome);
            post = (TextView) view.findViewById(R.id.lbPost);
            data = (TextView) view.findViewById(R.id.lbData);
            imgPost = (ImageView) view.findViewById(R.id.imgPost);

            u.setImagemPerfil(imgUsuario, contexto);
            galeria.setFoto(imgPost, contexto);

            imgUsuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(contexto, PerfilActivity.class);
                    intent.putExtra("usuario", u.getCodigo());
                    contexto.startActivity(intent);
                }
            });

            nome.setText(u.getNome());
            post.setText(galeria.getLegenda());

            String[] temp = galeria.getData().split(" ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataForm = sdf.format(date);

            if (temp[0].equals(dataForm)) {
                temp = temp[1].split(":");
                data.setText("postou uma foto às " + temp[0] + ":" + temp[1]);
            }else{
                temp = temp[0].split("-");
                data.setText("postou uma foto em " + Util.formatDataDDmesYYYY(galeria.getData()));
            }

            new CompartilharExternamente(contexto, view, "Comentario feito App TCC");

            callback.view(view);
            Log.i("*** OK","View de FOTO do Post ["+this.post.getCodigo()+"] adicionada.");

        }catch (Exception e){
            Log.e("*** ERRO", "View de FOTO não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }

    }
}
