package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackExcluir;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackSalvar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.interfaces.Retorno;
import es.esy.rafaelsilva.tcc.modelo.Compra;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Criado por Rafael em 07/11/2016, enjoy it.
 */
public class CtrlCompra implements Retorno {
    private Context contexto;
    private CallbackSalvar callbackSalvar;
    private int notificacao;
    private int carater;
    private int produto;
    private int pai;
    private String post;

    public CtrlCompra(Context contexto) {
        this.contexto = contexto;
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

    @Override
    public void trazer(int codigo, final CallbackTrazer callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "compra");
        params.put("condicao", "pai");
        params.put("valores", String.valueOf(codigo));

        GetData<Compra> getData = new GetData<>("objeto", params);
        getData.executar(Compra.class, new CallBackDAO() {
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
        params.put("tabela", "compra");
        params.put("ordenacao", parametro);

        GetData<Compra> getData = new GetData<>("lista", params);
        getData.executar(Compra.class, new CallBackDAO() {
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



    public void salvar(int notificacao, int carater, int produto, String post, final CallbackSalvar callbackSalvar){
        this.callbackSalvar = callbackSalvar;
        this.carater = carater;
        this.notificacao = notificacao;
        this.produto = produto;
        this.post = post;
        this.salvarUm();
    }

    private void salvarUm() {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "post");
        params.put("condicao", "usuario, status, tipo");
        params.put("valores", DadosUsuario.codigo+","+carater+","+4);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                salvarDois();
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

    private void salvarDois() {
        new CtrlPost(contexto).trazer("usuario = " + String.valueOf(DadosUsuario.codigo) +" ORDER BY codigo DESC LIMIT 1", new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Post rsp = (Post) obj;
                pai = rsp.getCodigo();
                salvarTres();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Falha ao postar", Toast.LENGTH_LONG).show();
                salvarErro();
            }
        });
    }

    private void salvarTres() {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "compra");
        params.put("condicao", "notificacao, carater, usuario, produto, pai, comentario");
        params.put("valores", notificacao+","+carater+","+DadosUsuario.codigo+","+produto+","+pai+",'"+post+"'");

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag()) {
                    callbackSalvar.resultadoSalvar(resposta);
                }else {
                    salvarErro();
                }
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callbackSalvar.falha();
                salvarErro();
            }
        });
    }

    private void salvarErro(){
        new CtrlPost(contexto).excluir(pai, new CallbackExcluir() {
            @Override
            public void resultadoExcluir(boolean flag) {
                if (flag)
                    Toast.makeText(contexto, "Não foi possível postar \nPost cancelado.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(contexto, "Não foi possível postar \nPost pendente.", Toast.LENGTH_LONG).show();
                callbackSalvar.falha();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Não foi possível postar \nPost pendente.", Toast.LENGTH_LONG).show();
                callbackSalvar.falha();
            }
        });
    }
}
