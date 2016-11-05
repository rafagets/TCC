package es.esy.rafaelsilva.tcc.modelo;

import java.util.Objects;

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
    private int editado;

    public int getEditado() {
        return editado;
    }

    public void setEditado(int editado) {
        this.editado = editado;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
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

    @Override
    public boolean equals(Object obj) {
        if (this != obj)
            return true;

        if (obj == null)
            return false;

        if (getClass() != obj.getClass())
            return false;

        Post post = (Post) obj;
        return Objects.equals(editado, post.editado)
                && Objects.equals(codigo, post.codigo)
                && Objects.equals(status, post.status)
                && Objects.equals(data, post.data)
                && Objects.equals(filha, post.filha)
                && Objects.equals(tipo, post.tipo)
                && Objects.equals(usuario, post.usuario);
    }
}
