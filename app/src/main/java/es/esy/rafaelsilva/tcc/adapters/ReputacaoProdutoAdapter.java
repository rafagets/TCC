package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.activity.Teste;
import es.esy.rafaelsilva.tcc.modelo.Avaliacao;
import es.esy.rafaelsilva.tcc.modelo.Historico;

/**
 * Created by Rafael on 09/09/2016.
 */
public class ReputacaoProdutoAdapter extends RecyclerView.Adapter<ReputacaoProdutoAdapter.MyViewHolder> {

    Context contexto;
    List<Teste> lista;

    public ReputacaoProdutoAdapter(Context contexto, List<Teste> lista) {
        this.contexto = contexto;
        this.lista = lista;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflar a activity aqui

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reputacao_produto, parent, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ReputacaoProdutoAdapter.MyViewHolder holder, int position) {
        // preenchimentos dos campos

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // declarar os campos

        public MyViewHolder(View itemView) {
            super(itemView);
            // instanciar os campos


        }
    }
}
