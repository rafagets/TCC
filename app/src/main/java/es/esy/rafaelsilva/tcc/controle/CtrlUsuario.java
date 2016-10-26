package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 21/10/2016.
 */
public class CtrlUsuario {

    private Context contexto;

    public CtrlUsuario(Context contexto) {
        this.contexto = contexto;
    }


    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Usuario> getData = new GetData<>("objeto", contexto, params);
        getData.executar(Usuario.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callback.resultadoTrazer(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callback.falha();
            }
        });

    }

    public void logar(String email, String senha, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("condicao", "email, senha");
        params.put("valores",email + " and " + senha);

        GetData<Usuario> getData = new GetData<>("objeto", contexto, params);
        getData.executar(Usuario.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callback.resultadoTrazer(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callback.falha();
            }
        });

    }

    public void listar(String parametro, final CallbackListar callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("ordenacao", parametro);

        GetData<Usuario> getData = new GetData<>("lista", contexto, params);
        getData.executar(Usuario.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {

            }

            @Override
            public void sucessoLista(List<Object> resposta) {
                callback.resultadoListar(resposta);
            }

            @Override
            public void erro(String resposta) {
                callback.falha();
            }
        });

    }

    public void salvar(String valores, String campos, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "usuario");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", contexto, params);
        getData.executar(Resposta.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callbackSalvar.resultadoSalvar(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {

            }
        });


    }

    public void excluir(int codigo){

    }

}
