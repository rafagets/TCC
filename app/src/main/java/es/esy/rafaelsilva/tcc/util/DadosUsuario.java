package es.esy.rafaelsilva.tcc.util;

import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Created by Rafael on 06/09/2016.
 */
public class DadosUsuario {

    public static int codigo = 1;
    public static String nome = "Sabrina";
    public static String email ;
    public static String senha;

    public static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuarioCorrente(Usuario usuarioCorrente) {
        DadosUsuario.usuario = usuarioCorrente;
        DadosUsuario.codigo = usuarioCorrente.getCodigo();
        DadosUsuario.nome = usuarioCorrente.getNome();
        DadosUsuario.email = usuarioCorrente.getEmail();
        DadosUsuario.senha = usuarioCorrente.getSenha();
    }
}
