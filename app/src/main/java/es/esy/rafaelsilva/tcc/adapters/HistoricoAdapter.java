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
import es.esy.rafaelsilva.tcc.modelo.Historico;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.Util;

/**
 * Created by Rafael on 12/09/2016.
 */
public class HistoricoAdapter extends RecyclerView.Adapter<HistoricoAdapter.MyViewHolder> {
    Context contexto;
    List<Historico> lista;

    public HistoricoAdapter(Context contexto, List<Historico> lista) {
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // preenchimentos dos campos
        Historico h = lista.get(position);

        holder.nome.setText(h.getTipoObj().getNome());
        holder.data.setText(Util.formatDataDDmesYYYY(h.getData()));
        holder.detalhe.setText(h.getNome());

        new ImageLoaderTask(holder.imgHistorico).execute(Config.caminhoImageIcons + h.getTipoObj().getImagem());

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // declarar os campos
        TextView nome, data, detalhe;
        ImageView imgHistorico;
        LinearLayout linha;

        public MyViewHolder(View itemView) {
            super(itemView);

            // instanciar os campos
            nome = (TextView) itemView.findViewById(R.id.lbNome);
            data = (TextView) itemView.findViewById(R.id.lbData);
            detalhe = (TextView) itemView.findViewById(R.id.lbDetalhes);
            linha = (LinearLayout) itemView.findViewById(R.id.linha);
            imgHistorico = (ImageView) itemView.findViewById(R.id.imgHistorico);

        }
    }
}
