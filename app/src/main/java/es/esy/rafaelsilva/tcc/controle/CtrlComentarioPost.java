package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;

/**
 * Created by Rafael on 21/10/2016.
 */
public class CtrlComentarioPost {

    private Context contexto;

    public CtrlComentarioPost(Context contexto) {
        this.contexto = contexto;
    }


    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "comentariopost");
        params.put("condicao", "comentario");
        params.put("valores", String.valueOf(codigo));

        GetData<ComentarioPost> getData = new GetData<>("objeto", params);
        getData.executar(ComentarioPost.class, new VolleyCallback() {
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
        params.put("tabela", "comentariopost");
        params.put("ordenacao", parametro);

        GetData<ComentarioPost> getData = new GetData<>("lista", params);
        getData.executar(ComentarioPost.class, new VolleyCallback() {
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

    public void salvar(ComentarioPost obj){

    }

    public void excluir(int codigo){

    }

}