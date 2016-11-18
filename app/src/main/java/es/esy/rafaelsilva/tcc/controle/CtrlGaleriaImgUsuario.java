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
import es.esy.rafaelsilva.tcc.modelo.GaleriaImgUsuario;
import es.esy.rafaelsilva.tcc.modelo.Post;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.UploadDeImagens;

/**
 * Criado por Rafael em 15/11/2016, enjoy it.
 */
public class CtrlGaleriaImgUsuario implements Retorno {
    private Context contexto;

    private CallbackSalvar call;
    private int pai, status;
    private String legenda, nomeImagem, path;

    public CtrlGaleriaImgUsuario(Context contexto) {
        this.contexto = contexto;
    }

    @Override
    public void trazer(int codigo, final CallbackTrazer callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "galeriaimgusuario");
        params.put("condicao", "pai");
        params.put("valores", String.valueOf(codigo));

        GetData<GaleriaImgUsuario> getData = new GetData<>("objeto", params);
        getData.executar(GaleriaImgUsuario.class, new CallBackDAO() {
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
        params.put("tabela", "galeriaimgusuario");
        params.put("ordenacao", parametro);

        GetData<GaleriaImgUsuario> getData = new GetData<>("lista", params);
        getData.executar(GaleriaImgUsuario.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {

            }

            @Override
            public void sucessoLista(List<Object> resposta) {
                if (resposta != null)
                    callback.resultadoListar(resposta);
                else
                    callback.falha();
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
        params.put("tabela", "galeriaimgusuario");
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
        params.put("tabela", "galeriaimgusuario");
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
    public void excluir(int codigo, final CallbackExcluir callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "D");
        params.put("tabela", "galeriaimgusuario");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

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



    public void postar(int status, String legenda, String path, CallbackSalvar call){
        this.status = status;
        this.legenda = legenda;
        this.path = path;
        this.call = call;

        this.criarPost();
    }

    private void criarPost() {
        String valores = "0, 5, "+DadosUsuario.codigo;
        String condicao = "status, tipo, usuario";
        new CtrlPost(contexto).salvar(condicao, valores, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                buscarPost();
            }

            @Override
            public void falha() {
                call.falha();
            }
        });
    }

    private void buscarPost() {
        new CtrlPost(contexto).trazer("usuario = " + String.valueOf(DadosUsuario.codigo) + " ORDER BY codigo DESC LIMIT 1", new CallbackTrazer() {
            @Override
            public void resultadoTrazer(Object obj) {
                Post post = (Post) obj;
                String[] data = post.getData().split(" ");
                pai = post.getCodigo();
                nomeImagem = pai +"-"+ post.getUsuario() +"-"+data[0]+".jpg";
                postarDadosImagem();
            }

            @Override
            public void falha() {
                call.falha();
            }
        });
    }

    private void postarDadosImagem() {
        String valores = "'"+nomeImagem +"',"+  status +","+ DadosUsuario.codigo +","+ pai +", '"+ legenda +"'";
        String campos = "img, status, usuario, pai, legenda";
        this.salvar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                atualizarPost();
            }

            @Override
            public void falha() {
                call.falha();
            }
        });
    }

    private void atualizarPost() {
        String valores = "status = "+ status;
        String campos = "codigo = "+ pai;
        new CtrlPost(contexto).atualizar(valores, campos, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                postarFoto();
            }

            @Override
            public void falha() {
                call.falha();
            }
        });
    }

    private void postarFoto() {
        new UploadDeImagens(contexto).enviar(path, nomeImagem, "post");
        call.resultadoSalvar(new Object());
    }
}
