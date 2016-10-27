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

public class PerfilSobre extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PerfilSobre newInstance(int sectionNumber) {
        PerfilSobre fragment = new PerfilSobre();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PerfilSobre() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_perfil_sobre, container, false);

        return rootView;
    }

}
