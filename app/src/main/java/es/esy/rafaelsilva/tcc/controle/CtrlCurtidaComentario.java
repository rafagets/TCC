package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.Retorno;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 21/10/2016.
 */
public class CtrlCurtidaComentario implements Retorno {

    private Context contexto;

    public CtrlCurtidaComentario(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<CurtidaComentario> getData = new GetData<>("objeto", params);
        getData.executar(CurtidaComentario.class, new CallBackDAO() {
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

    @Override
    public void listar(String parametro, final CallbackListar callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "curtidacomentario");
        params.put("ordenacao", parametro);

        GetData<CurtidaComentario> getData = new GetData<>("lista", params);
        getData.executar(CurtidaComentario.class, new CallBackDAO() {
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

    @Override
    public void salvar(String valores, String campos, CallbackSalvar callback) {

    }

    @Override
    public void atualizar(String valores, String campos, CallbackSalvar callbackSalvar) {

    }

    @Override
    public void excluir(int codigo, CallbackExcluir callback) {

    }



    public void excluir(int codigo, final CallbackSalvar callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "curtidacomentario");
        params.put("condicao", "usuario");
        params.put("valores", String.valueOf(DadosUsuario.codigo) + " AND comentario = " + codigo);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                callback.resultadoSalvar(resposta);
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

    public void curtir(int comentario, final int post, final CallbackSalvar callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "curtidacomentario");
        params.put("condicao", "comentario, usuario");
        params.put("valores", String.valueOf(comentario) +","+ DadosUsuario.codigo);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                atualizaPost(callback, post);
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

    private void atualizaPost(final CallbackSalvar callback, int post){
        new CtrlPost(contexto).atualizar("editado = editado + 1", "codigo="+ post, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                callback.resultadoSalvar(obj);
            }

            @Override
            public void falha() {

            }
        });
    }
}
