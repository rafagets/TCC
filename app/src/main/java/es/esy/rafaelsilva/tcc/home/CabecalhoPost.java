package es.esy.rafaelsilva.tcc.home;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import es.esy.rafaelsilva.tcc.R;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CabecalhoPost extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cabecalho_post, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout comentar = (LinearLayout) getActivity().findViewById(R.id.lyComentar);
        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postarComent();
            }
        });
    }

    private void postarComent() {

        AlertDialog.Builder mensagem = new AlertDialog.Builder(getActivity());

        // defino a view que contem os dados para abertura da mesa
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_post_coment, null);
        mensagem.setView(view);


        mensagem.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoge, int which) {


            }
        });

        mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "Ação cancelada.", Toast.LENGTH_LONG).show();

            }
        });

        mensagem.show();

        // FORÇA O TECLADO APARECER AO ABRIR O ALERT
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

}
