package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.AmizadeSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.CompartilharExternamente;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewAmizade {
    private AmizadeSP sp;
    private CallbackView callback;
    private View v;
    private Context contexto;
    private Post post;
    private Amigos a;
    private Usuario usu;
    private Usuario amigo;
    private boolean existeSP = false;
//    private List<ComentarioPost> cp; // ok
//    private List<CurtidaComentario> cc; // ok

    public ViewAmizade(Context contexo, View view, Post post) {
        this.contexto = contexo;
        this.post = post;
        this.v = view;
        this.sp = new AmizadeSP(contexto, "TCC_AMIZADE_"+post.getCodigo());
    }

    public void getView(CallbackView callback){
        this.callback = callback;

        Post postSP = sp.lerPost();
        if (postSP != null) {
            if (postSP.getEditado() == post.getEditado()) {
                a = sp.lerAmigos();
                usu = sp.lerAmiAdd();
                amigo = sp.lerAmiAce();

                existeSP = true;
                montar();
            }else {
                getAmizade();
            }
        }else{
            getAmizade();
        }
    }

    private void getAmizade(){
        new CtrlAmigos(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                a = (Amigos) obj;
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
            new CtrlUsuario(contexto).trazer(a.getAmigoAdd(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    usu = (Usuario) obj;
                    getAmigo();
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

    private void getAmigo(){
        try {
            new CtrlUsuario(contexto).trazer(a.getAmigoAce(), new CallbackTrazer() {
                @Override
                public void resultadoTrazer(Object obj) {
                    amigo = (Usuario) obj;
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

    private void montar(){
        if (!existeSP){
            sp.salvar(post,a,amigo,usu);
        }

        try {
            final TextView nome, data, nomeAmigo, profissaoAmigo, estiloAmigo;
            CircleImageView imgUsuario, imgAmigo;
            //final ImageView addOne, coment;

            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            data = (TextView) v.findViewById(R.id.lbData);
            nome.setText(usu.getNome());

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
                data.setText("às " + Util.formatHoraHHMM(post.getData()));
            else
                data.setText("em " + Util.formatDataDDmesYYYY(post.getData()));



            // dados amigo
            imgAmigo = (CircleImageView) v.findViewById(R.id.imgAmigo);
            nomeAmigo = (TextView) v.findViewById(R.id.lbNomeAmigo);
            profissaoAmigo = (TextView) v.findViewById(R.id.lbProfissaoAmigo);
            estiloAmigo = (TextView) v.findViewById(R.id.lbEstiloAmigo);

            if (amigo.getImagem() != null) {
                amigo.setImagemPerfil(imgAmigo, contexto);
            }
            nomeAmigo.setText(amigo.getNome());
            profissaoAmigo.setText(amigo.getProfissao());
            estiloAmigo.setText(amigo.getAlimentacao());

            monitorarCliqueImgAmigo(imgAmigo);

            new CompartilharExternamente(contexto, v, "Comentario feito App TCC");

            callback.view(v);
            Log.i("*** OK","View de AMIZADE do Post ["+this.post.getCodigo()+"] adicionada.");

        } catch (Exception e) {
            Log.e("*** ERRO", "View de AMIZADE não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }
    }


    private void monitorarCliqueImgAmigo(CircleImageView imgAmigo) {
        imgAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, PerfilActivity.class);
                intent.putExtra("usuario", amigo.getCodigo());
                contexto.startActivity(intent);
            }
        });
    }

}
