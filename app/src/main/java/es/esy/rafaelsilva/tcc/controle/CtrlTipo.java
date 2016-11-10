package es.esy.rafaelsilva.tcc.controle;

import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.esy.rafaelsilva.tcc.DAO.GetData;
import es.esy.rafaelsilva.tcc.interfaces.CallbackTrazer;
import es.esy.rafaelsilva.tcc.interfaces.CallBackDAO;
import es.esy.rafaelsilva.tcc.modelo.Tipo;

/**
 * Criado por Rafael em 08/11/2016, enjoy it.
 */
public class CtrlTipo {
    private Context contexto;

    public CtrlTipo(Context contexto) {
        this.contexto = contexto;
    }

    public void trazer(int codigo, final CallbackTrazer callback){

        Map<String, String> params = new HashMap<>();
        params.put("acao", "R");
        params.put("tabela", "tipo");
        params.put("condicao", "codigo");
        params.put("valores", String.valueOf(codigo));

        GetData<Tipo> getData = new GetData<>("objeto", params);
        getData.executar(Tipo.class, new CallBackDAO() {
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
}
