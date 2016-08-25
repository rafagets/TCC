package es.esy.rafaelsilva.tcc.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.esy.rafaelsilva.tcc.R;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CabecalhoPost extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cabecalho_post, container, false);
    }
}
