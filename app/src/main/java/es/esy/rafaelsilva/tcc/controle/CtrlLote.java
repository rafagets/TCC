package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.VolleyCallback;
import es.esy.rafaelsilva.tcc.modelo.Lote;

/**
 * Criado por Rafael em 08/11/2016, enjoy it.
 */
public class CtrlLote {
    private Context contexto;

    public CtrlLote(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(String lote, final CallbackTrazer callbackTrazer){
        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "lote");
        params.put("condicao", "codigo");
        params.put("valores", lote);

        GetData<Lote> getData = new GetData<>("objeto", params);
        getData.executar(Lote.class, new VolleyCallback() {
            @Override
            public void sucesso(Object resposta) {
                callbackTrazer.resultadoTrazer(resposta);
            }

            @Override
            public void sucessoLista(List<Object> resposta) {

            }

            @Override
            public void erro(String resposta) {
                callbackTrazer.falha();
            }
        });
    }
}
