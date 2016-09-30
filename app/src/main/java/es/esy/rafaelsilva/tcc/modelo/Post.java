package es.esy.rafaelsilva.tcc.modelo;

/**
 * Created by Rafael on 18/09/2016.
 */
public class Post {

    /**
     * codigo : 1
     * status : 1
     * data : 2016-09-18 11:06:28
     * filha : 1
     * tipo : 1
     */

    private int codigo;
    private int status;
    private String data;
    private int filha;
    private int tipo;
    private int usuario;
    private Usuario usuarioObj;
    private Comentario comentarioObj;
    private Amigos amigosObj;
    private Avaliacao avaliacaoObj;

    public Amigos getAmigosObj() {
        return amigosObj;
    }

    public void setAmigosObj(Amigos amigosObj) {
        this.amigosObj = amigosObj;
    }

    public Avaliacao getAvaliacaoObj() {
        return avaliacaoObj;
    }

    public void setAvaliacaoObj(Avaliacao avaliacaoObj) {
        this.avaliacaoObj = avaliacaoObj;
    }

    public Comentario getComentarioObj() {
        return comentarioObj;
    }

    public void setComentarioObj(Comentario comentarioObj) {
        this.comentarioObj = comentarioObj;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuarioObj() {
        return usuarioObj;
    }

    public void setUsuarioObj(Usuario usuarioObj) {
        this.usuarioObj = usuarioObj;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFilha() {
        return filha;
    }

    public void setFilha(int filha) {
        this.filha = filha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
