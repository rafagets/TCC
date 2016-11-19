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
import es.esy.rafaelsilva.tcc.modelo.Lote;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.modelo.Produto;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Notificacao;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Criado por Rafael em 07/11/2016, enjoy it.
 */
public class CtrlCompra implements Retorno {
    private Context contexto;
    private CallbackSalvar callbackSalvar;

    private int notificacao;
    private int carater;
    private int pai;
    private String post;

    private Produto produto;
    private Lote lote;
    private Compra compra;

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



    public void salvar(int carater, int notificacao, Produto produto, Lote lote, String post, final CallbackSalvar callbackSalvar){
        this.callbackSalvar = callbackSalvar;
        this.carater = carater;
        this.notificacao = notificacao;
        this.produto = produto;
        this.lote = lote;
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
        params.put("valores", notificacao+","+carater+","+DadosUsuario.codigo+","+produto.getCodigo()+","+pai+",'"+post+"'");

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag()) {
                    getCompra();
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

    private void getCompra(){
        this.trazer(pai, new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                compra = (Compra) obj;

                if (notificacao == 1)
                    agendarNotificacao();
            }

            @Override
            public void falha() {
                callbackSalvar.falha();
            }
        });
    }

    private void agendarNotificacao(){
        int ID_NOTIFICACAO = pai;
        String titulo = "Faltam 5 dias para vencer a validade!";
        String corpo = produto.getNome()+ " vence em " + Util.formatDataDDmesYYYY(lote.getDatavencimento());
        String subcorpo = " Clique e saiba mais ";
        new Notificacao(contexto, ID_NOTIFICACAO).enviar(compra.getCodigo(), titulo, corpo, subcorpo);
    }

}
