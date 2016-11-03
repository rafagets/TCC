package es.esy.rafaelsilva.tcc.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackView;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.task.ViewAmizade;
import es.esy.rafaelsilva.tcc.task.ViewAvaliacao;
import es.esy.rafaelsilva.tcc.task.ViewComentario;

public class PerfilAtividade extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public SwipeRefreshLayout recarregar;
    private LinearLayout layout;
    private List<Post> posts;
    private int usuario;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PerfilAtividade newInstance(int sectionNumber) {
        PerfilAtividade fragment = new PerfilAtividade();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PerfilAtividade() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_perfil_atividade, container, false);

        layout = (LinearLayout) rootView.findViewById(R.id.relativeLayout);
        recarregar = (SwipeRefreshLayout) rootView.findViewById(R.id.recarregar);
        recarregar.setRefreshing(true);

        carregarComentarios();

        recarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                carregarComentarios();
            }
        });

        return rootView;
    }

    public void carregarComentarios() {
        usuario = ((PerfilActivity) getActivity()).getUsuarioCodigo();
        new CtrlPost(getActivity()).listar("WHERE usuario = "+ usuario +" ORDER BY data DESC", new CallbackListar() {
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
