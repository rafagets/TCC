package es.esy.rafaelsilva.tcc.task;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CrtlAmigos;
import es.esy.rafaelsilva.tcc.controle.CrtlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 23/10/2016.
 */
public class ViewAmizade {
    private CallbackView callback;
    private View v;
    private Context contexto;
    private Post post;
    private Amigos a;
    private Usuario usu;
    private Usuario amigo;
//    private List<ComentarioPost> cp; // ok
//    private List<CurtidaComentario> cc; // ok

    public ViewAmizade(Context contexo, View view, Post post) {
        this.contexto = contexo;
        this.post = post;
        this.v = view;
    }

    public void getView(CallbackView callback){
        this.callback = callback;
        getAmizade();
    }

    private void getAmizade(){
        new CrtlAmigos(contexto).trazer(post.getCodigo(), new CallbackTrazer() {
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
        new CrtlUsuario(contexto).trazer(a.getAmigoAdd(), new CallbackTrazer() {
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
    }

    private void getAmigo(){
        new CrtlUsuario(contexto).trazer(a.getAmigoAce(), new CallbackTrazer() {
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
    }

    private void montar(){
        try {
            final TextView nome, data, nomeAmigo, profissaoAmigo, estiloAmigo;
            CircleImageView imgUsuario, imgAmigo;
            //final ImageView addOne, coment;

            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            data = (TextView) v.findViewById(R.id.lbData);
            nome.setText(usu.getNome());

            ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
            downImg.execute(Config.caminhoImageTumb + usu.getImagem());

            String[] temp = post.getData().split(" ");
            long date = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dataForm = sdf.format(date);

            if (temp[0].equals(dataForm))
                data.setText("fez uma amizade às " + Util.formatHoraHHMM(post.getData()));
            else
                data.setText("fez uma amizade em " + Util.formatDataDDmesYYYY(post.getData()));



            // dados amigo
            imgAmigo = (CircleImageView) v.findViewById(R.id.imgAmigo);
            nomeAmigo = (TextView) v.findViewById(R.id.lbNomeAmigo);
            profissaoAmigo = (TextView) v.findViewById(R.id.lbProfissaoAmigo);
            estiloAmigo = (TextView) v.findViewById(R.id.lbEstiloAmigo);

            ImageLoaderTask downImg2 = new ImageLoaderTask(imgAmigo);
            downImg2.execute(Config.caminhoImageTumb + amigo.getImagem());
            nomeAmigo.setText(amigo.getNome());
            profissaoAmigo.setText(amigo.getProfissao());
            estiloAmigo.setText(amigo.getAlimentacao());

            callback.view(v);
            Log.i("*** OK","View de AMIZADE do Post ["+this.post.getCodigo()+"] adicionada.");

        } catch (Exception e) {
            Log.e("*** ERRO", "View de AMIZADE não inserida (erro na montagem)");
            callback.view(null);
            e.printStackTrace();
        }
    }

}
