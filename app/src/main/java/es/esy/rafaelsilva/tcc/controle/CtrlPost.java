package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.modelo.Post;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlPost {
    private Context contexto;

    public CtrlPost(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "post");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Post> getData = new GetData<>("objeto", contexto, params);
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

        GetData<Post> getData = new GetData<>("lista", contexto, params);
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

    public void salvar(Post usuario){

    }

    public void excluir(int codigo){

    }
}
