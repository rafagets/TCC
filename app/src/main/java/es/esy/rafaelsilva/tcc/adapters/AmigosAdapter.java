package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Amigos;

/**
 * Created by Rafael on 26/10/2016.
 */
public class AmigosAdapter extends BaseAdapter {
    private Context contexto;
    private List<Amigos> amigos;

    public AmigosAdapter(List<Amigos> amigos, Context contexto) {
        this.amigos = amigos;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return amigos.size();
    }

    @Override
    public Object getItem(int i) {
        return amigos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Amigos amigo = amigos.get(i);

        View v = LayoutInflater.from(contexto).inflate(R.layout.adapter_amigos, viewGroup, false);
        TextView nome = (TextView) v.findViewById(R.id.lbNome);
        ImageView imgAmigo = (ImageView) v.findViewById(R.id.imgUsuario) ;
        nome.setText(String.valueOf(amigo.getCodigo()));

        return v;
    }
}
