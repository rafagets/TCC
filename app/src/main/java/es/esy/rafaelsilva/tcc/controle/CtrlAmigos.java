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
import es.esy.rafaelsilva.tcc.modelo.Amigos;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlAmigos implements Retorno {
    private Context contexto;
    private int codigoAmizade; // guarda o codigo do amigo
    private int papa; // guarda o codigo do post em que a amizade é vinculada.
    private CallbackSalvar callbackSalvar;
    private CallbackExcluir callbackExcluir;


    public CtrlAmigos(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void salvar(String valores, String campos, final CallbackSalvar callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "amigos");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag()) {
                    callback.resultadoSalvar(resposta);
                }else{
                    callback.falha();
                }
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
    public void atualizar(String valores, String campos, final CallbackSalvar callbackSalvar) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "U");
        params.put("tabela", "amigos");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag())
                    callbackSalvar.resultadoSalvar(resposta);
                else
                    callbackSalvar.falha();
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

    @Override
    public void excluir(int codigo, CallbackExcluir callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "amigos");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag())
                    excluirDois();
                else
                    callbackExcluir.resultadoExcluir(false);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callbackExcluir.falha();
            }
        });
    }

    @Override
    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "amigos");
        params.put("condicao", "pai");
        params.put("valores", String.valueOf(codigo));

        GetData<Amigos> getData = new GetData<>("objeto", params);
        getData.executar(Amigos.class, new CallBackDAO() {
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
        params.put("tabela", "amigos");
        params.put("ordenacao", parametro);

        GetData<Amigos> getData = new GetData<>("lista", params);
        getData.executar(Amigos.class, new CallBackDAO() {
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


    public void AddAmigo(int codigoAmizade, final CallbackSalvar callbackSalvar){
        this.callbackSalvar = callbackSalvar;
        this.codigoAmizade = codigoAmizade;
        postarUm();
    }

    public void aceitarAmizade(int codigoAmizade, int papa, final CallbackSalvar callback){
        this.callbackSalvar = callback;
        this.codigoAmizade = codigoAmizade;
        this.papa = papa;
        this.aceitar();
    }

    public void excluirAmizade(int codigoAmizade, int pai, CallbackExcluir callbackExcluir){
        this.codigoAmizade = codigoAmizade;
        this.callbackExcluir = callbackExcluir;
        this.papa = pai;
        this.excluir(codigoAmizade, callbackExcluir);
    }

    public void contar (String parametro, final CallbackTrazer callback){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "amigos");
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

            }
        });
    }



    /* Inicio da logica de aceitacao de uma nova amizade*/
    /* Faz um post */
    private void aceitar() {
        String valores = "statusAmizade = 0";
        String campos = "codigo = "+codigoAmizade;
        this.atualizar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                aceitarDois();
            }

            @Override
            public void falha() {
                callbackSalvar.falha();
            }
        });

    }

    // atualiza o post para status visivel
    private void aceitarDois() {
        String valores = "status = 1";
        String campos = "codigo = "+papa;

        new CtrlPost(contexto).atualizar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                callbackSalvar.resultadoSalvar(obj);
            }

            @Override
            public void falha() {
                callbackSalvar.falha();
            }
        });
    }



    /* Inicio da logica de adição de uma nova amizade*/
    /* Faz um post */
    private void postarUm(){
        new CtrlPost(contexto).salvar("usuario, tipo, status", String.valueOf(DadosUsuario.codigo) +","+ 2 +","+ 0, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.isFlag())
                    postarDois();
                else
                    Toast.makeText(contexto, "Falha ao postar", Toast.LENGTH_LONG).show();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Falha ao postar", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* Busca o codigo do post feito anteriormente */
    private void postarDois(){
        new CtrlPost(contexto).trazer("usuario = " + String.valueOf(DadosUsuario.codigo) +" ORDER BY codigo DESC", new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Post rsp = (Post) obj;
                postarTres(rsp.getCodigo());
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Falha ao postar", Toast.LENGTH_LONG).show();
            }
        });
    }

    /* Salva a amizade */
    private void postarTres(final int pai){
        String valores = DadosUsuario.codigo +","+ codigoAmizade +","+ pai;
        String campos = "amigoAdd, amigoAce, pai";

        this.salvar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                callbackSalvar.resultadoSalvar(obj);
            }

            @Override
            public void falha() {
                postarQuatro(pai);
            }
        });
    }

    /* Caso de algum erro, anula a amizade */
    private void postarQuatro(int pai){
        new CtrlPost(contexto).excluir(pai, new CallbackExcluir() {
            @Override
            public void resultadoExcluir(boolean flag) {
                if (flag)
                    Toast.makeText(contexto, "Não foi possível postar \nPost cancelado.", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(contexto, "Não foi possível postar \nPost pendente.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Não foi possível postar \nPost pendente.", Toast.LENGTH_LONG).show();
            }
        });
    }


    /* Inicio da logica de exclusao de uma amizade */
    /* Exclui a amizade e logo em seguida exclui o post em que é vinculado no passo dois*/
    private void excluirDois(){
        new CtrlPost(contexto).excluir(papa, new CallbackExcluir() {
            @Override
            public void resultadoExcluir(boolean flag) {
                if (flag)
                    callbackExcluir.resultadoExcluir(true);
                else
                    callbackExcluir.resultadoExcluir(false);
            }

            @Override
            public void falha() {
                callbackExcluir.falha();
            }
        });
    }

}
