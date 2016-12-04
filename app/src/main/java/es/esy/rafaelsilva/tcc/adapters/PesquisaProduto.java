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
import es.esy.rafaelsilva.tcc.activity.DetalhesProdutoActivity;
import es.esy.rafaelsilva.tcc.modelo.Produto;

/**
 * Criado por Rafael em 02/11/2016, enjoy it.
 */
public class PesquisaProduto extends BaseAdapter implements Filterable {
    private Context contexto;

    private List<Produto> listaP;
    private List<Produto> listaFiltroP; /*Lista usado para fazer a filtragem dos dados digitados pelo usuario*/



    public PesquisaProduto(Context contextoP, List<Produto> listaP) {
        this.contexto = contextoP;
        this.listaP = listaP;
        this.listaFiltroP = listaP;
    }

    @Override
    public int getCount() {
        return listaFiltroP.size();
    }

    @Override
    public Object getItem(int i) {
        return listaFiltroP.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View v = LayoutInflater.from(contexto).inflate(R.layout.adapter_pesquisa_produto, viewGroup, false);
        CircleImageView img = (CircleImageView) v.findViewById(R.id.imgProduto);
        TextView nomeProduto = (TextView) v.findViewById(R.id.lbNomeProduto);

        listaFiltroP.get(i).setImgIcone(img, contexto);
        nomeProduto.setText(listaFiltroP.get(i).getNome());
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(contexto, DetalhesProdutoActivity.class);
                intent.putExtra("produto", listaFiltroP.get(i).getCodigo());
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
                    results.count = listaP.size();
                    results.values = listaP;
                } else {//do the search
                    List<Produto> resultsData = new ArrayList<>();
                    String searchStr = charSequence.toString().toUpperCase();
                    for (Produto produto : listaP)
                        if (produto.getNome().toUpperCase().contains(searchStr)) resultsData.add(produto);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }

                return results;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listaFiltroP = (ArrayList<Produto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
