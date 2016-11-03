package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.PerfilActivity;
import es.esy.rafaelsilva.tcc.modelo.Usuario;

/**
 * Criado por Rafael em 02/11/2016, enjoy it.
 */
public class Pesquisa extends BaseAdapter implements Filterable {
    private Context contexto;
    private List<Usuario> lista;
    private List<Usuario> listaFiltro; /*Lista usado para fazer a filtragem dos dados digitados pelo usuario*/

    public Pesquisa(Context contexto, List<Usuario> lista) {
        this.contexto = contexto;
        this.lista = lista;
        this.listaFiltro = lista;
    }

    @Override
    public int getCount() {
        return listaFiltro.size();
    }

    @Override
    public Object getItem(int i) {
        return listaFiltro.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(contexto).inflate(R.layout.adapter_pesquisa, viewGroup, false);
        CircleImageView img = (CircleImageView) v.findViewById(R.id.imgUsuario);
        TextView nomeUsuario = (TextView) v.findViewById(R.id.lbNome);

        listaFiltro.get(i).setImagemPerfil(img, contexto);
        nomeUsuario.setText(listaFiltro.get(i).getNome());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, PerfilActivity.class);
                intent.putExtra("usuario", listaFiltro.get(i).getCodigo());
                contexto.startActivity(intent);
            }
        });

        return v;
    }

    /*Esse metodo é o responsavel pela a magica da filtragem da tela de pesquisa*/
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                FilterResults results = new FilterResults();

                if (charSequence == null || charSequence.length() == 0) {
                    //no constraint given, just return all the data. (no search)
                    results.count = lista.size();
                    results.values = lista;
                } else {//do the search
                    List<Usuario> resultsData = new ArrayList<>();
                    String searchStr = charSequence.toString().toUpperCase();
                    for (Usuario usuario : lista)
                        if (usuario.getNome().toUpperCase().contains(searchStr)) resultsData.add(usuario);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }

                return results;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaFiltro = (ArrayList<Usuario>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
