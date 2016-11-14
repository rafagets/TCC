package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.Retorno;
import es.esy.rafaelsilva.tcc.modelo.ComentarioAvaliacao;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Criado por Rafael em 12/11/2016, enjoy it.
 */
public class CtrlComentarioAvaliacao implements Retorno {
    private Context contexto;
    private int pai;

    public CtrlComentarioAvaliacao(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void trazer(int codigo, CallbackTrazer callback) {

    }

    @Override
    public void listar(String parametro, final CallbackListar callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "comentarioavaliacao");
        params.put("ordenacao", parametro);

        GetData<ComentarioAvaliacao> getData = new GetData<>("lista", params);
        getData.executar(ComentarioAvaliacao.class, new CallBackDAO() {
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
    public void salvar(String valores, String campos, final CallbackSalvar callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "comentarioavaliacao");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta resp = (Resposta) resposta;
                if (resp.isFlag())
                    atualizaPost(callback);
                else
                    callback.falha();
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
    public void atualizar(String valores, String campos, CallbackSalvar callbackSalvar) {

    }

    @Override
    public void excluir(int codigo, CallbackExcluir callback) {

    }

    public void comentar (String valores, String campos, int pai, final CallbackSalvar callback){
        this.salvar(valores, campos, callback);
        this.pai = pai;
    }

    private void atualizaPost(final CallbackSalvar callback){
        new CtrlPost(contexto).atualizar("editado = editado + 1", "codigo="+ pai, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta resp = (Resposta) obj;
                if (resp.isFlag())
                    callback.resultadoSalvar(obj);
                else
                    callback.falha();
            }

            @Override
            public void falha() {
                callback.falha();
            }
        });
    }
}
