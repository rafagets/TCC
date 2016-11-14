package es.esy.rafaelsilva.tcc.interfaces;

/**
 * Criado por Rafael em 12/11/2016, enjoy it.
 */
public interface Retorno {
    void trazer(int codigo, final CallbackTrazer callback);
    void listar(String parametro, final CallbackListar callback);
    void salvar(String valores, String campos, final CallbackSalvar callback);
    void atualizar(String valores, String campos, final CallbackSalvar callbackSalvar);
    void excluir(int codigo, final CallbackExcluir callback);
}
