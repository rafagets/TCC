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
import es.esy.rafaelsilva.tcc.modelo.Usuario;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;
import es.esy.rafaelsilva.tcc.util.Resposta;
import es.esy.rafaelsilva.tcc.util.UploadDeImagens;

/**
 * Criado por Rafael em 21/10/2016, enjoy it.
 */
public class CtrlUsuario implements Retorno {

    private Context contexto;
    private String path, nome;
    private CallbackSalvar call;

    public CtrlUsuario(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "usuario");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Usuario> getData = new GetData<>("objeto", params);
        getData.executar(Usuario.class, new CallBackDAO() {
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
        params.put("tabela", "usuario");
        params.put("ordenacao", parametro);

        GetData<Usuario> getData = new GetData<>("lista", params);
        getData.executar(Usuario.class, new CallBackDAO() {
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
    public void salvar(String valores, String campos, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "C");
        params.put("tabela", "usuario");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta resp = (Resposta) resposta;
                if (resp.isFlag())
                    callbackSalvar.resultadoSalvar(resp);
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
    public void atualizar(String valores, String campos, final CallbackSalvar callbackSalvar){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "U");
        params.put("tabela", "usuario");
        params.put("condicao", campos);
        params.put("valores", valores);

        GetData<Resposta> getData = new GetData<>("objeto", params);
        getData.executar(Resposta.class, new CallBackDAO() {
            @Override
            public void sucesso(Object resposta) {
                Resposta resp = (Resposta) resposta;
                if (resp.isFlag())
                    callbackSalvar.resultadoSalvar(resp);
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
    public void excluir(int codigo, CallbackExcluir callbackExcluir) {

    }


    public void logar(String email, String senha, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "L");
        params.put("tabela", "usuario");
        params.put("condicao", "senha");
        params.put("valores", "'" +senha+ "' AND email = '" +email+ "'");

        GetData<Usuario> getData = new GetData<>("objeto", params);
        getData.executar(Usuario.class, new CallBackDAO() {
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

    public void fotoPerfil(String path, String nome, final CallbackSalvar call){
        this.call = call;
        this.path = path;
        this.nome = nome;

        String condicao = "codigo = " + DadosUsuario.codigo;
        String valores = "imagem = '" +nome+ "'";
        this.atualizar(valores, condicao, new CallbackSalvar() {
            @Override
            public void resultadoSalvar(Object obj) {
                uploadImagem();
            }

            @Override
            public void falha() {
                call.falha();
            }
        });


//        if (DadosUsuario.usuario.getImagem().equals(nome) && DadosUsuario.usuario.getImagem() != null){
//            new UploadDeImagens(contexto).enviar(path, nome, "perfil");
//            call.resultadoSalvar(new Object());
//        }else {
//            String condicao = "codigo = " + DadosUsuario.codigo;
//            String valores = "imagem = " + nome;
//            this.atualizar(condicao, valores, new CallbackSalvar() {
//                @Override
//                public void resultadoSalvar(Object obj) {
//                    uploadImagem();
//                }
//
//                @Override
//                public void falha() {
//                    call.falha();
//                }
//            });
//        }
    }

    private void uploadImagem() {
        new UploadDeImagens(contexto).enviar(path, nome, "perfil");
        call.resultadoSalvar(new Object());
    }

}
