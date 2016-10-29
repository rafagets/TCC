package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlPost {
    private Context contexto;

    public CtrlPost(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(String condicao, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "post");
        params.put("ordenacao", "WHERE "+ condicao);

        GetData<Post> getData = new GetData<>("objeto", params);
        getData.executar(Post.class, new VolleyCallback() {
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
        params.put("tabela", "post");
        params.put("ordenacao", parametro);

        GetData<Post> getData = new GetData<>("lista", params);
        getData.executar(Post.class, new VolleyCallback() {
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

    public void salvar(String condicao, String valores, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "post");
        params.put("condicao", condicao);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
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
                callbackSalvar.falha();
            }
        });
    }

    public void excluir(int codigo, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "post");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Resposta> getData = new GetData<>("objeto", params);
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
                callbackSalvar.falha();
            }
        });
    }
}
