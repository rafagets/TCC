package es.esy.rafaelsilva.tcc.util;

import android.content.Context;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import es.esy.rafaelsilva.tcc.R;

/**
 * Criado por Rafael em 13/11/2016, enjoy it.
 */
public class UploadDeImagens {
    private Context contexto;

    public UploadDeImagens(Context contexto) {
        this.contexto = contexto;
    }

    public void enviar(String path, String nomeImagem, String tipo){
        String caminho ="";
        if (tipo.equals("perfil"))
            caminho = "img/tumb/";

        try {
            //Creating a multi part request
            new MultipartUploadRequest(contexto, "1", Config.uploadImgPerfil)
                    .addFileToUpload(path, "imagem") //Adding file
                    .addParameter("nomeFoto", caminho + nomeImagem) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig()
                            .setTitle("Salvando imagem")
                            .setErrorMessage("Falha ao salvar no servidor")
                            .setIcon(R.mipmap.ic_launcher)
                            .setCompletedMessage("Salvo com sucesso!"))
                    .setMaxRetries(2)
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(contexto, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
