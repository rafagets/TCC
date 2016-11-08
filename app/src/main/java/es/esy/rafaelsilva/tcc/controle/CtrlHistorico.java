package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackListar;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.modelo.Historico;

/**
 * Created by Rafa on 07/11/2016.
 */
public class CtrlHistorico {

    private Context contexto;

    public CtrlHistorico(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(int lote, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "historico");
        params.put("condicao", "lote");
        params.put("valores", String.valueOf(lote));

        GetData<Historico> getData = new GetData<>("objeto", params);
        getData.executar(Historico.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callback.resultadoTrazer(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {
                //callback.resultadoTrazer(resposta);
            }

            @Override
            public void erro(String resposta) {
                callback.falha();
            }
        });

    }

    public void listar(String lote, final CallbackListar callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "historico");
        params.put("condicao", "lote");
        params.put("valores", String.valueOf(lote));

        GetData<Historico> getData = new GetData<>("lista", params);
        getData.executar(Historico.class, new VolleyCallback() {
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
