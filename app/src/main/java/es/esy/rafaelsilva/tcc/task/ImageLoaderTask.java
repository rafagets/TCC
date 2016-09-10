package es.esy.rafaelsilva.tcc.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * Created by Rafael on 21/08/2016.
 */
public class ImageLoaderTask extends AsyncTask<String, Void, Bitmap> {
    ImageView img;

    public ImageLoaderTask(ImageView img) {
        this.img = img;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap foto = null;

        try {
            InputStream imput = new java.net.URL(url).openStream();
            foto = BitmapFactory.decodeStream(imput);
        } catch (MalformedURLException e) {
            Log.e("ERRO","url errada");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("ERRO","Img não baixada");
            e.printStackTrace();
        }
        return foto;
    }

    @Override
    protected void onPostExecute(Bitmap foto) {
        img.setImageBitmap(foto);
    }
}
