package es.esy.rafaelsilva.tcc.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadFile;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

        redimencionar(new File(path));

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

    public File redimencionar(File file){
        try {

            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 4;
            // factor of downsizing the image

            FileInputStream inputStream = new FileInputStream(file);
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();

            // The new size we want to scale to
            final int REQUIRED_SIZE=75;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);

            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100 , outputStream);

            return file;
        } catch (Exception e) {
            return null;
        }
    }

}
