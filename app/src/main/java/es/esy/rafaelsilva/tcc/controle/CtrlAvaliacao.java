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
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlAvaliacao implements Retorno {
    private Context contexto;
    private CallbackSalvar call;
    private int status, produto, pai;
    private float estrelas;
    private String comentario;

    public CtrlAvaliacao(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void salvar(String valores, String campos, final CallbackSalvar callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "avaliacao");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag()) {
                    callback.resultadoSalvar(resposta);
                }else {
                    salvarErro();
                }
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callback.falha();
                salvarErro();
            }
        });
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
        params.put("tabela", "avaliacao");
        params.put("condicao", "pai");
        params.put("valores", String.valueOf(codigo));

        GetData<Avaliacao> getData = new GetData<>("objeto", params);
        getData.executar(Avaliacao.class, new CallBackDAO() {
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
        params.put("tabela", "avaliacao");
        params.put("ordenacao", parametro);

        GetData<Avaliacao> getData = new GetData<>("lista", params);
        getData.executar(Avaliacao.class, new CallBackDAO() {
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



    public void contar (String parametro, final CallbackTrazer callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "avaliacao");
        params.put("asteristico", "count(codigo)");
        params.put("ordenacao", "WHERE "+ parametro);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
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

    public void postar(int status, float estrelas, String comentario, int produto, CallbackSalvar call){
        this.call = call;
        this.status = status;
        this.estrelas = estrelas;
        this.comentario = comentario;
        this.produto = produto;

        salvarUm();
    }

    private void salvarUm() {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "post");
        params.put("condicao", "usuario, status, tipo");
        params.put("valores", DadosUsuario.codigo+","+status+","+3);

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
                call.falha();
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
        String campos = "estrelas, status, comentario, usuario, produto, pai";
        String valores = String.valueOf(estrelas)+","+
                status+","+
                "'"+comentario+"',"+
                String.valueOf(DadosUsuario.codigo)+","+
                produto+","+
                pai;

        this.salvar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.isFlag()){
                    call.resultadoSalvar(obj);
                }else {
                    call.falha();
                }
            }

            @Override
            public void falha() {
                call.falha();
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
                call.falha();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Não foi possível postar \nPost pendente.", Toast.LENGTH_LONG).show();
                call.falha();
            }
        });
    }
}
