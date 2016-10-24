package es.esy.rafaelsilva.tcc.interfaces;

import java.util.List;

/**
 * Created by Rafael on 19/10/2016.
 */
public interface VolleyCallback {

    void sucesso(Object resposta);
    void sucessoLista(List<Object> resposta);
    void erro(String resposta);

}
