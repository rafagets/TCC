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
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.interfaces.Retorno;
import es.esy.rafaelsilva.tcc.modelo.Lote;

/**
 * Criado por Rafael em 08/11/2016, enjoy it.
 */
public class CtrlLote implements Retorno {
    private Context contexto;

    public CtrlLote(Context contexto) {
        this.contexto = contexto;
    }


    @Override
    public void trazer(int codigo, final CallbackTrazer callback) {
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "lote");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Lote> getData = new GetData<>("objeto", params);
        getData.executar(Lote.class, new CallBackDAO() {
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
    public void listar(String parametro, CallbackListar callback) {

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
}
