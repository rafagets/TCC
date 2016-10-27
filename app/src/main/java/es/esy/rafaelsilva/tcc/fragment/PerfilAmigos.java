package es.esy.rafaelsilva.tcc.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.adapters.AmigosAdapter;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.modelo.Amigos;

public class PerfilAmigos extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Amigos> amigos;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PerfilAmigos newInstance(int sectionNumber) {
        PerfilAmigos fragment = new PerfilAmigos();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PerfilAmigos() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_perfil_amigos, container, false);

        new CtrlAmigos(getActivity()).listar("", new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                amigos = new ArrayList<>();
                for (Object obj : lista)
                    amigos.add((Amigos) obj);

                AmigosAdapter adapter = new AmigosAdapter(amigos, getActivity());
                GridView gridView = (GridView) getActivity().findViewById(R.id.gridView);
                gridView.setAdapter(adapter);

            }

            @Override
            public void falha() {

            }
        });

        return rootView;
    }

}
