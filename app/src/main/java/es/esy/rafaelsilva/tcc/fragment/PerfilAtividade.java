package es.esy.rafaelsilva.tcc.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.task.MontarView;

public class PerfilAtividade extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    public SwipeRefreshLayout recarregar;
    private LinearLayout layout;
    private List<Post> posts;

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

        new CtrlPost(getActivity()).listar("WHERE usuario = "+ 2 +" ORDER BY data DESC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                posts = new ArrayList<>();
                for (Object obj : lista)
                    posts.add((Post) obj);

                new MontarView(getActivity(), layout, posts, recarregar).execute();
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
//        PostTask postTask = new PostTask(getActivity(), recarregar);
//        postTask.execute("R", "post", "ORDER BY data DESC");
    }
}
