package es.esy.rafaelsilva.tcc.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.AtualizaCadastroUsuarioActivity;

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

        Button editar = (Button) rootView.findViewById(R.id.btnEditarDados);
        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AtualizaCadastroUsuarioActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }

}
