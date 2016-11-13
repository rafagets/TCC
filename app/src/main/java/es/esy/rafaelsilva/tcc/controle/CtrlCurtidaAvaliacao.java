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
import es.esy.rafaelsilva.tcc.modelo.CurtidaAvaliacao;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Criado por Rafael em 12/11/2016, enjoy it.
 */
public class CtrlCurtidaAvaliacao implements Retorno {
    private Context contexto;
    private int pai;

    public CtrlCurtidaAvaliacao(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    public void trazer(int codigo, final CallbackTrazer callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "curtidaavaliacao");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<CurtidaAvaliacao> getData = new GetData<>("objeto", params);
        getData.executar(CurtidaAvaliacao.class, new CallBackDAO() {
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
    public void listar(String parametro, final CallbackListar callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "curtidaavaliacao");
        params.put("ordenacao", parametro);

        GetData<CurtidaAvaliacao> getData = new GetData<>("lista", params);
        getData.executar(CurtidaAvaliacao.class, new CallBackDAO() {
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
    public void excluir(int codigo, final CallbackExcluir callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "curtidaavaliacao");
        params.put("condicao", "usuario");
        params.put("valores", String.valueOf(DadosUsuario.codigo) + " AND comentario = " + codigo);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag())
                    callback.resultadoExcluir(true);
                else
                    callback.resultadoExcluir(false);
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



    public void excluir(int codigo, int pai, final CallbackExcluir callback) {
        this.pai = pai;
        this.excluir(codigo, callback);
        this.atualizaPost(new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {

            }

            @Override
            public void falha() {

            }
        }, pai);
    }

    public void curtir(int avaliacao, final int post, final CallbackSalvar callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "curtidaavaliacao");
        params.put("condicao", "avaliacao, usuario");
        params.put("valores", String.valueOf(avaliacao) +","+ DadosUsuario.codigo);

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
