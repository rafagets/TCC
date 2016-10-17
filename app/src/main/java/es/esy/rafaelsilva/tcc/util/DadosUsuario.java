package es.esy.rafaelsilva.tcc.util;

import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Created by Rafael on 06/09/2016.
 */
public class DadosUsuario {

    public static int codigo = 1;
    public static String nome = "Sabrina";
//    public static String email ;
//    public static String
    public static Usuario usuarioCorrente;

    public static Usuario getUsuarioCorrente() {
        return usuarioCorrente;
    }

    public static void setUsuarioCorrente(Usuario usuarioCorrente) {
        DadosUsuario.usuarioCorrente = usuarioCorrente;
        DadosUsuario.codigo = usuarioCorrente.getCodigo();
        DadosUsuario.nome = usuarioCorrente.getNome();
    }
}
