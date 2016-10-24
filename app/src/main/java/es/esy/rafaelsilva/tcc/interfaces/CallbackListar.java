package es.esy.rafaelsilva.tcc.interfaces;

import java.util.List;

/**
 * Created by Rafael on 22/10/2016.
 */
public interface CallbackListar {
    void resultadoListar(List<Object> lista);
    void falha();
}
