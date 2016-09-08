package es.esy.rafaelsilva.tcc.home;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.GenericDAO.LoadPosts;
import es.esy.rafaelsilva.tcc.R;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CorpoHome extends Fragment {

    SwipeRefreshLayout recarregar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.corpo_home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recarregar = (SwipeRefreshLayout) getActivity().findViewById(R.id.recarregar);
        recarregar.setRefreshing(true);

        carregarComentarios();

        recarregar.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                carregarComentarios();
            }
        });}


    void carregarComentarios() {
        LoadPosts loadPosts = new LoadPosts(getActivity(), recarregar);
        loadPosts.execute("R", "comentario", "ORDER BY data DESC");
    }


}
