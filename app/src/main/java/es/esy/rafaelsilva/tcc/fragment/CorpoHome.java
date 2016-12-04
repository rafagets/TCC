package es.esy.rafaelsilva.tcc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.CompraSP;
import es.esy.rafaelsilva.tcc.DAO.SharedPreferences.ListaPostsSP;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.views.ViewAmizade;
import es.esy.rafaelsilva.tcc.views.ViewAvaliacao;
import es.esy.rafaelsilva.tcc.views.ViewComentario;
import es.esy.rafaelsilva.tcc.views.ViewCompra;
import es.esy.rafaelsilva.tcc.views.ViewPostFoto;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CorpoHome extends Fragment {

    public SwipeRefreshLayout recarregar;
    private LinearLayout layout;
    private List<Post> posts;
    private boolean flag = false;
    private List<Amigos> listaAmigos;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_corpo_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        layout = (LinearLayout) getActivity().findViewById(R.id.relativeLayout);
        recarregar = (SwipeRefreshLayout) getActivity().findViewById(R.id.recarregar);
        recarregar.setRefreshing(true);

        //carregarComentarios();
        carregarAmigos();

        recarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                //carregarComentarios();
                carregarAmigos();
            }
        });

    }

    private void carregarAmigos() {
        int u = DadosUsuario.codigo;
        new CtrlAmigos(getActivity()).listar("WHERE amigoAce = "+u+" OR amigoAdd = "+u+" AND statusAmizade = 0",
                new CallbackListar() {
                    @Override
                    public void resultadoListar(List<Object> lista) {

                        if (lista != null) {
                            listaAmigos = new ArrayList<>();
                            for (Object l : lista)
                                listaAmigos.add((Amigos) l);
                        }

                        carregarComentarios();
                    }

                    @Override
                    public void falha() {
                        Toast.makeText(getActivity(), "Que pena, você ainda não tem amigos\nFaça novas amizades!", Toast.LENGTH_LONG).show();
//                        recarregar.setRefreshing(false);
//                        ImageView falha = new ImageView(getActivity());
//                        //falha.setImageResource(R.drawable.back_falha_carregar);
//                        falha.setImageResource(R.drawable.ic_exclude_amigo);
//                        layout.addView(falha);

                        carregarComentarios();
                    }
                });
    }

    public String concatenarAmigos(List<Amigos> listaAmigos){
        String concat = "";
        for (Amigos a : listaAmigos) {
            if (a.getAmigoAdd() == DadosUsuario.codigo)
                concat += a.getAmigoAce() + ",";
            else
                concat += a.getAmigoAdd() + ",";
        }
        concat += DadosUsuario.codigo;

        return concat;
    }


    public void carregarComentarios() {

        String sql;
        if (listaAmigos != null)
            sql = "WHERE usuario in(" + concatenarAmigos(listaAmigos) + ") AND status = 1 ORDER BY data DESC";
        else
            sql = "WHERE usuario = " +DadosUsuario.codigo+ " AND status = 1 ORDER BY data DESC";

        new CtrlPost(getActivity()).listar(sql, new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                posts = new ArrayList<>();
                for (Object obj : lista)
                    posts.add((Post) obj);

                if (posts.size() > 0) {

                    ListaPostsSP sp = new ListaPostsSP(getActivity(), "TCC_POST_"+DadosUsuario.codigo);
                    List<Post> postsSP = sp.lerPosts();

                    if (postsSP == null) {
                        sp.salvar(posts);
                    } else {
                        if (posts.equals(postsSP)) {
                            flag = true;
                        } else {
                            sp.salvar(posts);
                        }
                    }

                    montarView(0);
                }
            }

            @Override
            public void falha() {

                try {
                    Toast.makeText(getActivity(),
                            "Falha ao conectar com servidor \uD83D\uDE22As funções foram limitadas ",
                            Toast.LENGTH_LONG).show();

                    posts = new ListaPostsSP(getActivity(), "TCC_POST_"+DadosUsuario.codigo).lerPosts();
                    montarView(0);
                } catch (Exception e) {
                    ImageView falha = new ImageView(getActivity());
                    falha.setImageResource(R.drawable.falha);
                    layout.addView(falha);

                    falha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            carregarComentarios();
                            layout.removeAllViews();
                            recarregar.setRefreshing(true);
                        }
                    });
                }
                recarregar.setRefreshing(false);
            }
        });

    }

    private void montarView(final int posicao){
        if (posicao <= posts.size() - 1) {
            if (posts.get(posicao).getTipo() == 1) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_post, null);
                new ViewComentario(getActivity(), view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montarView(posicao + 1);
                        }else{
                            Log.e("*** ERRO","Erro inserir post["+posicao+"]-> codigo ["+posts.get(posicao).getCodigo()+"]");
                            montarView(posicao + 1);
                        }
                    }
                });
            }
            else if (posts.get(posicao).getTipo() == 2){
                View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_add_amigo, null);
                new ViewAmizade(getActivity(), view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montarView(posicao + 1);
                        }else{
                            Log.e("*** ERRO","Erro inserir post["+posicao+"]-> codigo ["+posts.get(posicao).getCodigo()+"]");
                            montarView(posicao + 1);
                        }
                    }
                });
            }
            else if (posts.get(posicao).getTipo() == 3){
                View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_avaliacao, null);
                new ViewAvaliacao(getActivity(), view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montarView(posicao + 1);
                        }else{
                            Log.e("*** ERRO","Erro inserir post["+posicao+"]-> codigo ["+posts.get(posicao).getCodigo()+"]");
                            montarView(posicao + 1);
                        }
                    }
                });
            }
            else if (posts.get(posicao).getTipo() == 4){
                View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_post_compra, null);
                new ViewCompra(getActivity(), view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montarView(posicao + 1);
                        }else{
                            Log.e("*** ERRO","Erro inserir post["+posicao+"]-> codigo ["+posts.get(posicao).getCodigo()+"]");
                            montarView(posicao + 1);
                        }
                    }
                });
            }
            else if (posts.get(posicao).getTipo() == 5){
                View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_post_foto, null);
                new ViewPostFoto(getActivity(), view, posts.get(posicao)).getView(new CallbackView() {
                    @Override
                    public void view(View view) {
                        if (view != null) {
                            layout.addView(view);
                            montarView(posicao + 1);
                        }else{
                            Log.e("*** ERRO","Erro inserir post["+posicao+"]-> codigo ["+posts.get(posicao).getCodigo()+"]");
                            montarView(posicao + 1);
                        }
                    }
                });
            }
            else {
                montarView(posicao + 1);
            }
        }else{
            recarregar.setRefreshing(false);
        }
    }

    private void excluirDados(){
        ListaPostsSP sp = new ListaPostsSP(getActivity(), "TCC_POST_"+DadosUsuario.codigo);
        List<Post> psp = sp.lerPosts();

        for (Post p : psp){
            if (p.getTipo() == 1) {

            }else if (p.getTipo() == 2){

            }else if (p.getTipo() == 3){

            }else if (p.getTipo() == 4){
                CompraSP s = new CompraSP(getActivity(), "TCC_COMPRA_"+p.getCodigo());
            }else if (p.getTipo() == 5){

            }

        }
    }

}
