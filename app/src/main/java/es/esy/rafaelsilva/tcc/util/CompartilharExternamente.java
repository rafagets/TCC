package es.esy.rafaelsilva.tcc.util;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import es.esy.rafaelsilva.tcc.R;

/**
 * Criado por Rafael em 19/11/2016, enjoy it.
 */
public class CompartilharExternamente {
    public CompartilharExternamente(final Context contexto, View v, final String menssagem) {
        ImageView imgShare = (ImageView) v.findViewById(R.id.imgShare);

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, menssagem);
                sendIntent.setType("text/plain");
                contexto.startActivity(sendIntent);
            }
        });
    }
}
