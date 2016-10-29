package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlAmigos {
    private Context contexto;

    public CtrlAmigos(Context contexto) {
        this.contexto = contexto;
    }


    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "amigos");
        params.put("condicao", "pai");
        params.put("valores", String.valueOf(codigo));

        GetData<Amigos> getData = new GetData<>("objeto", params);
        getData.executar(Amigos.class, new VolleyCallback() {
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
        params.put("tabela", "amigos");
        params.put("ordenacao", parametro);

        GetData<Amigos> getData = new GetData<>("lista", params);
        getData.executar(Amigos.class, new VolleyCallback() {
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

    public void salvar(Amigos usuario){

    }

    public void excluir(int codigo){

    }

    public void contar (String parametro, final CallbackTrazer callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "amigos");
        params.put("asteristico", "count(codigo)");
        params.put("ordenacao", "WHERE "+ parametro);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callback.resultadoTrazer(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {

            }
        });
    }
}
