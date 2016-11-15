package es.esy.rafaelsilva.tcc.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.adapters.AmigosAdapter;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

public class PerfilAmigos extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private List<Amigos> amigos;
    private int usuario;

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
        final View rootView = inflater.inflate(R.layout.fragment_perfil_amigos, container, false);

        /* Busca o usuario alvo da pesquisa*/
        usuario = ((PerfilActivity) getActivity()).getUsuarioCodigo();
        /* Busca a lista de amigos e monta o adapter*/
        buscar(rootView);

        return rootView;

    }

    private void buscar(final View rootView){
        /*
        * verifica se o usuario da pesquisa é o mesmo que esta logado
        * se ja estiver logado, buscas todos os amigos, inclusive as solicitaçoes.
        */
        String condicao = "";
        if (usuario != DadosUsuario.codigo){
            condicao = "WHERE amigoAdd = " +usuario+ " OR amigoAce = " +usuario+ " AND statusAmizade = 0 ORDER BY statusAmizade DESC";
        }else{
            condicao = "WHERE amigoAdd = " +usuario+ " OR amigoAce = " +usuario;
        }

        new CtrlAmigos(getActivity()).listar(condicao, new CallbackListar() {
            @Override
            public void resultadoListar(List<Object> lista) {
                amigos = new ArrayList<>();
                for (Object obj : lista)
                    amigos.add((Amigos) obj);

                if (amigos != null) {
                    AmigosAdapter adapter = new AmigosAdapter(amigos, getActivity());
                    GridView gridView = (GridView) getActivity().findViewById(R.id.gridView);
                    gridView.setAdapter(adapter);
                }else{
                    LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
                    layout.removeAllViewsInLayout();
                    ImageView falha = new ImageView(getActivity());
                    falha.setImageResource(R.drawable.back_falha_carregar);
                    layout.addView(falha);
                }

            }

            @Override
            public void falha() {
                LinearLayout layout = (LinearLayout) rootView.findViewById(R.id.linearLayout);
                layout.removeAllViewsInLayout();
                TextView semAmigos = new TextView(getActivity());
                semAmigos.setText("Nenhum amigo encontrado");

                /*ImageView falha = new ImageView(getActivity());
                falha.setImageResource(R.drawable.back_falha_carregar);
                layout.addView(falha);*/
                layout.addView(semAmigos);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

}
