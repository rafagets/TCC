package es.esy.rafaelsilva.tcc.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.task.PostComentarioTask;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 25/08/2016.
 */
public class CabecalhoPost extends Fragment {

    EditText comentario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cabecalho_post, container, false);
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_post_coment, null);
        mensagem.setView(view);
        comentario = (EditText) view.findViewById(R.id.txtPost);

        mensagem.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoge, int which) {

                if (!comentario.getText().toString().equals("")) {

                    //"C", CabecalhoPost.this, "post"
                    PostComentarioTask task = new PostComentarioTask("C", comentario.getText().toString(),"post", getActivity());
                    String campo = "usuario";
                    String valor = String.valueOf(DadosUsuario.codigo);
                    task.execute(campo, valor);

                }else{
                    Toast.makeText(getActivity(), "Digite um comentário.", Toast.LENGTH_LONG).show();
                }

            }
        });

        mensagem.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), "Ação cancelada.", Toast.LENGTH_LONG).show();

            }
        });

        mensagem.show();

//        // FORÇA O TECLADO APARECER AO ABRIR O ALERT
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

    }

    public void postarComentario(String post){

        UtilTask task = new UtilTask(getActivity(), "C", "comentario");
        String campo = "comentario,usuarioPost,pai";
        String valor = "\"" + comentario.getText().toString() + "\"," + DadosUsuario.codigo + "," + post;
        task.execute(campo, valor);

    }

}
