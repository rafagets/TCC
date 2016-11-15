package es.esy.rafaelsilva.tcc.fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.HomeActivity;
import es.esy.rafaelsilva.tcc.activity.MainActivity;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.controle.CtrlAmigos;
import es.esy.rafaelsilva.tcc.controle.CtrlComentario;
import es.esy.rafaelsilva.tcc.controle.CtrlPost;
import es.esy.rafaelsilva.tcc.controle.CtrlUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.task.PostComentarioTask;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Criado por Rafael em 25/08/2016, enjoy it.
 */
public class CabecalhoPost extends Fragment {

    private EditText comentario;
    private CircleImageView imgUsuarioPrincipal;
    private LinearLayout layout;
    private TextView notificacao;
    private Usuario usuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cabecalho_post, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LinearLayout comentar = (LinearLayout) getActivity().findViewById(R.id.lyComentar);

        imgUsuarioPrincipal = (CircleImageView) getActivity().findViewById(R.id.imgUsuarioPrincipal);
        layout = (LinearLayout) getActivity().findViewById(R.id.linear_notificacao);
        notificacao = (TextView) getActivity().findViewById(R.id.notificacao);

        getUsuario();

        imgUsuarioPrincipal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PerfilActivity.class);
                startActivity(intent);
            }
        });

        comentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (usuario != null)
                    postarComent();
                else {
                    Toast.makeText(getActivity(), "Sem conexão\nTente mais tarde.", Toast.LENGTH_LONG).show();
                    getUsuario();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotificacoes();
    }

    private void postarComent() {

        AlertDialog.Builder mensagem = new AlertDialog.Builder(getActivity());

        // defino a view que contem os dados para abertura da mesa
        View view = getActivity().getLayoutInflater().inflate(R.layout.inflater_post_coment, null);

        comentario = (EditText) view.findViewById(R.id.txtPost);
        CircleImageView imgUsuarioLogado = (CircleImageView) view.findViewById(R.id.imgUsuarioLogado);
        Spinner spinner = (Spinner) view.findViewById(R.id.status);

        String[] status = new String[] {"Público","Amigos","Privado"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, status);
        spinner.setAdapter(adapter);

        if (usuario != null)
            usuario.setImagemPerfil(imgUsuarioLogado, getActivity());

        mensagem.setView(view);
        mensagem.setPositiveButton("Postar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialoge, int which) {

                if (!comentario.getText().toString().equals("")) {

                    // inicio a sequencia que ira realizar um post
                    postarUm();

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

    public void postarUm(){
        new CtrlPost(getActivity()).postar("usuario", String.valueOf(DadosUsuario.codigo), comentario.getText().toString(), new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.isFlag())
                    Toast.makeText(getActivity(), "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getActivity(), "Falha ao postar", Toast.LENGTH_LONG).show();
            }

            @Override
            public void falha() {
                Toast.makeText(getActivity(), "Falha ao postar", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getUsuario() {
        new CtrlUsuario(getActivity()).trazer(DadosUsuario.codigo, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                usuario = (Usuario) obj;
                if (usuario == null){
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("error", true);
                    startActivity(intent);
                    getActivity().finish();
                }else {
                    usuario.setImagemPerfil(imgUsuarioPrincipal, getActivity());
                    getNotificacoes();
                }
            }

            @Override
            public void falha() {

            }
        });
    }

    public void getNotificacoes(){
        new CtrlAmigos(getActivity()).contar("amigoAdd = " + DadosUsuario.codigo + " AND statusAmizade = 1", new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.getValor() > 0){
                    notificacao.setText(String.valueOf(rsp.getValor()));
                    layout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void falha() {

            }
        });
    }
}
