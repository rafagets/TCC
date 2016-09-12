package es.esy.rafaelsilva.tcc.activity;

/**
 * Created by Rafael on 10/09/2016.
 */
public class Teste {


    /**
     * codigo : 1
     * data : 2016-09-0511:48:53
     * status : 1
     * comentario : Bom dia!
     * usuarioPost : 2
     */

    private int codigo;
    private String data;
    private int status;
    private String comentario;
    private int usuarioPost;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getUsuarioPost() {
        return usuarioPost;
    }

    public void setUsuarioPost(int usuarioPost) {
        this.usuarioPost = usuarioPost;
    }
}
