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
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;

/**
 * Created by Rafael on 23/10/2016.
 */
public class CtrlPost {
    private Context contexto;

    public CtrlPost(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(String condicao, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "post");
        params.put("ordenacao", "WHERE "+ condicao);

        GetData<Post> getData = new GetData<>("objeto", params);
        getData.executar(Post.class, new CallBackDAO() {
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

        GetData<Post> getData = new GetData<>("lista", params);
        getData.executar(Post.class, new CallBackDAO() {
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

    public void salvar(String condicao, String valores, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "post");
        params.put("condicao", condicao);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                callbackSalvar.resultadoSalvar(resposta);
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

    public void excluir(int codigo, final CallbackExcluir callbackExcluir){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "post");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta rsp = (Resposta) resposta;
                if (rsp.isFlag())
                    callbackExcluir.resultadoExcluir(true);
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
    
    public void postar(String condicao, String valores, final String comentarioFeito, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "post");
        params.put("condicao", condicao);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                postarDois(comentarioFeito);
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

    private void postarDois(final String comentarioFeito){
        this.trazer("usuario = " + String.valueOf(DadosUsuario.codigo) +" ORDER BY codigo DESC LIMIT 1", new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Post rsp = (Post) obj;
                postarTres(rsp.getCodigo(), comentarioFeito);
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "Falha ao postar", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void postarTres(final int pai, String comentarioFeito){
        new CtrlComentario(contexto).salvar(pai, comentarioFeito, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                Resposta rsp = (Resposta) obj;
                if (rsp.isFlag())
                    Toast.makeText(contexto, "\uD83D\uDC4D", Toast.LENGTH_LONG).show();
            }

            @Override
            public void falha() {
                Toast.makeText(contexto, "falha", Toast.LENGTH_LONG).show();
                postarQuatro(pai);
            }
        });
    }

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
}
