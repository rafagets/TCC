package es.esy.rafaelsilva.tcc.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.ComentariosPostActivity;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;
import es.esy.rafaelsilva.tcc.DAO.DAO;
import es.esy.rafaelsilva.tcc.fragment.CorpoHome;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 18/09/2016.
 */
public class PostTask extends AsyncTask<String, Void, Boolean> {

    private SwipeRefreshLayout recarregar;
    private Context contexto;
    private List<Post> lista;
    private Activity home;

    public PostTask(Context contexto, SwipeRefreshLayout recarregar) {
        this.contexto = contexto;
        this.home = ((HomeActivity) contexto);
        this.recarregar = recarregar;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(String... values) {

        String[] params;
        JSONArray jsonArray;
        DAO helper;

        params = new String[] { "acao", "tabela", "ordenacao" };

        helper = new DAO();

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, params, values);

            try {
                lista = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    String json = jsonArray.get(i).toString();

                    Post obj;
                    Gson gson = new Gson();
                    obj = gson.fromJson(json, Post.class);
                    obj.setUsuarioObj(getUsuario(String.valueOf(obj.getUsuario())));

                    if (obj.getTipo() == 1) {
                        obj.setComentarioObj(this.loadCoemntario(String.valueOf(obj.getCodigo())));
                    }else if (obj.getTipo() == 2) {
                        obj.setAmigosObj(this.loadAmizade(String.valueOf(obj.getCodigo())));
                        //obj.getAmigosObj().setAmigoAceObj(new Usuario().getUsuarioObj(obj.getAmigosObj().getAmigoAce()));
                        obj.getAmigosObj().setAmigoAceObj(getUsuario(String.valueOf(obj.getAmigosObj().getAmigoAce())));
                    }else if (obj.getTipo() == 3){
                        obj.setAvaliacaoObj(this.loadAvaliacao(String.valueOf(obj.getCodigo())));
                    }
                    lista.add(obj);
                }

                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e){
            //e.printStackTrace();

        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        recarregar.setRefreshing(false);

        // implementação do historico
        final LinearLayout layout = (LinearLayout) home.findViewById(R.id.relativeLayout);
        // caso haja alguma view essa é destruida
        layout.removeAllViews();

        if (flag)

            try {
                for (int i = 0; i < lista.size(); i++) {
                    Post post = lista.get(i);

                    if (lista.get(i).getTipo() == 1)
                        inflarComentarios(post, layout);
                    else if (lista.get(i).getTipo() == 2)
                        this.inflarAmizade(post, layout);
                    else if (lista.get(i).getTipo() == 3)
                        this.inflarAvaliacao(post, layout);

                }
            } catch (Exception e){

                //e.printStackTrace();

                ImageView falha = new ImageView(contexto);
                falha.setImageResource(R.drawable.back_falha_carregar);
                layout.addView(falha);

                falha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        layout.removeAllViews();
                        recarregar = (SwipeRefreshLayout) home.findViewById(R.id.recarregar);
                        recarregar.setRefreshing(true);
                        ((CorpoHome) home.getFragmentManager().findFragmentById(R.id.corpo_home)).carregarComentarios();

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
                    ((CorpoHome) home.getFragmentManager().findFragmentById(R.id.corpo_home)).carregarComentarios();

                }
            });

        }
    }

    private void inflarAvaliacao(Post p, LinearLayout layout){
        View v = home.getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);

        final TextView nome, data, produto, avaliacao;
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
        Avaliacao av = p.getAvaliacaoObj();
        Produto prod = av.getProdutoObj();
        imgProduto = (CircleImageView) v.findViewById(R.id.imgProduto);
        produto = (TextView) v.findViewById(R.id.lbProduto);
        avaliacao = (TextView) v.findViewById(R.id.lbAvaliacao);
        estrela = (RatingBar) v.findViewById(R.id.estrelas);

        ImageLoaderTask downImg2 = new ImageLoaderTask(imgProduto);
        downImg2.execute(Config.caminhoImageProdutos + prod.getImgicone());

        produto.setText(prod.getNome());
        avaliacao.setText(av.getComentario());
        estrela.setRating(av.getEstrelas());

        layout.addView(v);
    }

    private void inflarAmizade(Post p, LinearLayout layout){
        View v = home.getLayoutInflater().inflate(R.layout.inflater_add_amigo, null);

        final TextView nome, data, nomeAmigo, profissaoAmigo, estiloAmigo;
        CircleImageView imgUsuario, imgAmigo;
        //final ImageView addOne, coment;
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
            data.setText("fez uma amizade às " + Util.formatHoraHHMM(p.getData()));
        else
            data.setText("fez uma amizade em " + Util.formatDataDDmesYYYY(p.getData()));



        // dados amigo
        Amigos a = p.getAmigosObj();
        Usuario amigo = p.getAmigosObj().getAmigoAceObj();
        imgAmigo = (CircleImageView) v.findViewById(R.id.imgAmigo);
        nomeAmigo = (TextView) v.findViewById(R.id.lbNomeAmigo);
        profissaoAmigo = (TextView) v.findViewById(R.id.lbProfissaoAmigo);
        estiloAmigo = (TextView) v.findViewById(R.id.lbEstiloAmigo);

        ImageLoaderTask downImg2 = new ImageLoaderTask(imgAmigo);
        downImg2.execute(Config.caminhoImageTumb + amigo.getImagem());
        nomeAmigo.setText(amigo.getNome());
        profissaoAmigo.setText(amigo.getProfissao());
        estiloAmigo.setText(amigo.getAlimentacao());

        layout.addView(v);
    }

    private void inflarComentarios(Post p, LinearLayout layout) {

        View v = home.getLayoutInflater().inflate(R.layout.inflater_post, null);

        final TextView nome, post, data, qtdAddOne, numComent;
        CircleImageView imgUsuario;
        final ImageView addOne, coment;
        Usuario usu = p.getUsuarioObj();
        final Comentario c = p.getComentarioObj();

        imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
        nome = (TextView) v.findViewById(R.id.lbNome);
        post = (TextView) v.findViewById(R.id.lbPost);
        data = (TextView) v.findViewById(R.id.lbData);
        addOne = (ImageView) v.findViewById(R.id.imgAddOne);
        coment = (ImageView) v.findViewById(R.id.imgComentarios);
        qtdAddOne = (TextView) v.findViewById(R.id.lbAddOne);
        numComent = (TextView) v.findViewById(R.id.lbComentarios);

        final boolean[] flag = {false};
        int curtido = 0;

        ImageLoaderTask downImg = new ImageLoaderTask(imgUsuario);
        downImg.execute(Config.caminhoImageTumb + usu.getImagem());

        nome.setText(usu.getNome());
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

        if (c.getCurtidaComentario() != null)
            for (int i=0; i < c.getCurtidaComentario().length; i++) {
                CurtidaComentario cc = c.getCurtidaComentario()[i];
                if (cc.getUsuario() == DadosUsuario.codigo){
                    addOne.setImageResource(R.drawable.ic_added);
                    i = c.getCurtidaComentario().length;
                    flag[0] = true;
                    curtido = 1;
                }
            }

        if (c.getCurtidaComentario() != null) {
            qtdAddOne.setText(String.valueOf(c.getCurtidaComentario().length) + " curtiu");
        }else {
            qtdAddOne.setText("");
        }

        // exclui curtida
        addOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (flag[0]) {
                    UtilTask util = new UtilTask(view.getContext(), "D", "curtidacomentario");
                    util.execute("usuario", String.valueOf(DadosUsuario.codigo) + " AND comentario = " + c.getCodigo());

                    addOne.setImageResource(R.drawable.ic_add_one);

                    int curtiu = 0;
                    if (c.getCurtidaComentario() != null)
                        curtiu = c.getCurtidaComentario().length - 1;

                    qtdAddOne.setText(String.valueOf(curtiu) + " curtiu");

                    flag[0] = false;
                }

                //Toast.makeText(view.getContext(), "long "+String.valueOf(holder.curtido), Toast.LENGTH_LONG).show();
                return true;

            }
        });

        //add curtida
        final int finalCurtido = curtido;
        addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!flag[0]) {
                    UtilTask util = new UtilTask(view.getContext(), "C", "curtidacomentario");
                    String campos = "comentario,usuario";
                    String values = c.getCodigo() + "," + DadosUsuario.codigo;
                    util.execute(campos, values);

                    addOne.setImageResource(R.drawable.ic_added);

                    int curtiu = 0;
                    if (c.getCurtidaComentario() != null)
                        curtiu = c.getCurtidaComentario().length + 1 - finalCurtido;
                    else
                        curtiu = 1;

                    qtdAddOne.setText(String.valueOf(curtiu) + " você curtiu");

                    flag[0] = true;

                }

                //Toast.makeText(view.getContext(), "curto "+String.valueOf(holder.curtido), Toast.LENGTH_LONG).show();

            }
        });



        if (c.getComentariosPost() != null){
            coment.setImageResource(R.drawable.ic_comented);
            numComent.setText(String.valueOf(c.getComentariosPost().length) + " comentou");
        }else{
            numComent.setText("");
        }


        coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //List<ComentarioPost> list = new ArrayList<>(Arrays.asList(c.getComentariosPost()));
                Intent intent = new Intent(contexto, ComentariosPostActivity.class);
                intent.putExtra("post", c.getCodigo());
                //intent.putExtra("comentarios", list);
                contexto.startActivity(intent);

            }
        });

        layout.addView(v);
    }



    private Comentario loadCoemntario(String pai) {

        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "comentario", "pai",  pai};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            String json = jsonArray.get(0).toString();

            Comentario obj;
            Gson gson = new Gson();
            obj = gson.fromJson(json, Comentario.class);

//            obj.setQtdCurtidas(countCurtidas(obj.getCodigo()));
//            obj.setQtdComentarios(countComentarios(obj.getCodigo()));

            obj.setCurtidaComentario(loadCurtidas(obj.getCodigo()));
            obj.setComentariosPost(getCoemntariosPost(obj.getCodigo()));
            return obj;

        }catch (Exception e){

        }

        return null;

    }

    private Avaliacao loadAvaliacao(String pai) {

        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "avaliacao", "pai",  pai};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            String json = jsonArray.get(0).toString();

            Avaliacao obj;
            Gson gson = new Gson();
            obj = gson.fromJson(json, Avaliacao.class);

            obj.setProdutoObj(this.getProduto(obj.getProduto()));
            return obj;

        }catch (Exception e){

        }

        return null;

    }

    private Amigos loadAmizade(String pai) {

        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "amigos", "pai",  pai};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            String json = jsonArray.get(0).toString();

            Amigos obj;
            Gson gson = new Gson();
            obj = gson.fromJson(json, Amigos.class);

            //List<CurtidaComentario> cc = loadCurtidas(obj.getCodigo());
            //obj.setCurtidaComentario(loadCurtidas(obj.getCodigo()));
            return obj;

        }catch (Exception e){

        }

        return null;

    }

    private CurtidaComentario[] loadCurtidas(int codigo) {

        CurtidaComentario[] curtida;
        JSONArray jsonArray;
        DAO helper = new DAO();


        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "curtidacomentario", "comentario",  String.valueOf(codigo)};

        try {

            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            curtida = new CurtidaComentario[jsonArray.length()];

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    String json = jsonArray.get(i).toString();

                    CurtidaComentario obj;
                    Gson gson = new Gson();
                    obj = gson.fromJson(json, CurtidaComentario.class);
                    curtida[i] = obj;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return curtida;

        }catch (Exception e){

        }

        return null;
    }


//    private int countCurtidas(int codigo){
//        JSONArray jsonArray;
//        DAO helper = new DAO();
//
//        String[] p = new String[] { "acao", "tabela", "asteristico", "condicao", "valores"  };
//        String[] v = new String[] { "R", "curtidacomentario", "count(codigo)", "comentario",  String.valueOf(codigo)};
//
//        try {
//
//            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
//
//            try {
//
//                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
//
//                return jsonObject.getInt("valor");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }catch (Exception e){
//
//        }
//
//        return 0;
//    }
//
//    private int countComentarios(int codigo){
//        JSONArray jsonArray;
//        DAO helper = new DAO();
//
//        String[] p = new String[] { "acao", "tabela", "asteristico", "condicao", "valores"  };
//        String[] v = new String[] { "R", "comentariopost", "count(codigo)", "coment",  String.valueOf(codigo) };
//
//        try {
//
//            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
//
//            try {
//
//                JSONObject jsonObject = (JSONObject) jsonArray.get(0);
//
//                return jsonObject.getInt("valor");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//        }catch (Exception e){
//
//        }
//
//        return 0;
//    }


    private Usuario getUsuario(String codigo) {

        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "usuario", "codigo",  codigo};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            String json = jsonArray.get(0).toString();

            Usuario obj;
            Gson gson = new Gson();
            obj = gson.fromJson(json, Usuario.class);
            return obj;

        }catch (Exception e){

        }

        return null;
    }

    private ComentarioPost[] getCoemntariosPost(int codigo) {

        JSONArray jsonArray;
        DAO helper = new DAO();


        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "comentariopost", "coment",  String.valueOf(codigo)};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            ComentarioPost[] comentariosPost = new ComentarioPost[jsonArray.length()];

            try {

                for (int i = 0; i < jsonArray.length(); i++) {
                    String json = jsonArray.get(i).toString();

                    ComentarioPost obj;
                    Gson gson = new Gson();
                    obj = gson.fromJson(json, ComentarioPost.class);
                    comentariosPost[i] = obj;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return comentariosPost;

        }catch (Exception e){

        }

        return null;

    }
    
    private Produto getProduto(int codigo){
        JSONArray jsonArray;
        DAO helper = new DAO();

        String[] p = new String[] { "acao", "tabela", "condicao", "valores"  };
        String[] v = new String[] { "R", "produto", "codigo",  String.valueOf(codigo)};

        try {
            jsonArray = helper.getJSONArray(Config.urlMaster, p, v);
            String json = jsonArray.get(0).toString();

            Produto obj;
            Gson gson = new Gson();
            obj = gson.fromJson(json, Produto.class);
            
            return obj;

        }catch (Exception e){

        }
        return null;
    }

}
