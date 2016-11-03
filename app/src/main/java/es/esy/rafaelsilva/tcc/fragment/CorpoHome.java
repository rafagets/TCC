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

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.task.ViewAmizade;
import es.esy.rafaelsilva.tcc.task.ViewAvaliacao;
import es.esy.rafaelsilva.tcc.task.ViewComentario;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CorpoHome extends Fragment {

    public SwipeRefreshLayout recarregar;
    private LinearLayout layout;
    private List<Post> posts;

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

        carregarComentarios();

        recarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                carregarComentarios();
            }
        });

    }


    public void carregarComentarios() {

        new CtrlPost(getActivity()).listar("ORDER BY data DESC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                posts = new ArrayList<>();
                for (Object obj : lista)
                    posts.add((Post) obj);

                if (posts.size() > 0)
                    montarView(0);
            }

            @Override
            public void falha() {
                ImageView falha = new ImageView(getActivity());
                falha.setImageResource(R.drawable.back_falha_carregar);
                layout.addView(falha);

                falha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        carregarComentarios();
                        layout.removeAllViews();
                        recarregar.setRefreshing(true);
                    }
                });
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
            else {
                montarView(posicao + 1);
            }
        }else{
            recarregar.setRefreshing(false);
        }
    }

}
