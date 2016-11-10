package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.modelo.Historico;

/**
 * Criado por Rafael em 08/11/2016, enjoy it.
 */
public class CtrlHistorico {
    private Context contexto;

    public CtrlHistorico(Context contexto) {
        this.contexto = contexto;
    }

    public void listar(String parametro, final CallbackListar callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "historico");
        params.put("ordenacao", parametro);

        GetData<Historico> getData = new GetData<>("lista", params);
        getData.executar(Historico.class, new CallBackDAO() {
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
}
