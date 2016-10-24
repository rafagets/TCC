package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;

/**
 * Created by Rafael on 21/10/2016.
 */
public class CrtlCurtidaComentario {

    private Context contexto;

    public CrtlCurtidaComentario(Context contexto) {
        this.contexto = contexto;
    }


    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<CurtidaComentario> getData = new GetData<>("objeto", contexto, params);
        getData.executar(CurtidaComentario.class, new VolleyCallback() {
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
        params.put("tabela", "curtidacomentario");
        params.put("ordenacao", parametro);

        GetData<CurtidaComentario> getData = new GetData<>("lista", contexto, params);
        getData.executar(CurtidaComentario.class, new VolleyCallback() {
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

    public void excluir(int codigo){
        UtilTask util = new UtilTask(contexto, "D", "curtidacomentario");
        util.execute("usuario", String.valueOf(DadosUsuario.codigo) + " AND comentario = " + codigo);
    }

    public void curtir(int comentario){
        UtilTask util = new UtilTask(contexto, "C", "curtidacomentario");
        String campos = "comentario,usuario";
        String values = comentario + "," + DadosUsuario.codigo;
        util.execute(campos, values);
    }

}
