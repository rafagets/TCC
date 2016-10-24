package es.esy.rafaelsilva.tcc.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.controle.CrtlPost;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.task.MontarView;

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

        new CrtlPost(getActivity()).listar("ORDER BY data DESC", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                posts = new ArrayList<>();
                for (Object obj : lista)
                    posts.add((Post) obj);

                new MontarView(getActivity(), layout, posts, recarregar).execute();
            }

            @Override
            public void falha() {
                recarregar.setRefreshing(false);
            }
        });
//        PostTask postTask = new PostTask(getActivity(), recarregar);
//        postTask.execute("R", "post", "ORDER BY data DESC");
    }


}
