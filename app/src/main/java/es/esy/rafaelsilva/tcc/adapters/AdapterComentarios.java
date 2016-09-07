package es.esy.rafaelsilva.tcc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.GenericDAO.ImageLoader;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.beans.Comentario;
import es.esy.rafaelsilva.tcc.configuracao.Config;
import es.esy.rafaelsilva.tcc.configuracao.DadosUsuario;

/**
 * Created by Rafael on 07/09/2016.
 */
public class AdapterComentarios extends RecyclerView.Adapter<AdapterComentarios.MyViewHolder> {


    private List<Comentario> comentarios;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome, post, data, qtdAddOne;
        public CircleImageView imgUsuario;
        public ImageView addOne;

        public MyViewHolder(View v) {
            super(v);
            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            post = (TextView) v.findViewById(R.id.lbPost);
            data = (TextView) v.findViewById(R.id.lbData);
            addOne = (ImageView) v.findViewById(R.id.imgAddOne);
            qtdAddOne = (TextView) v.findViewById(R.id.lbAddOne);
        }
    }


    public AdapterComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.corpo_home_post, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Comentario c = comentarios.get(position);
        ImageLoader downImg = new ImageLoader(holder.imgUsuario);
        downImg.execute(Config.caminhoImageTumb + c.getUsuario().getImagem());

        holder.nome.setText(c.getUsuario().getNome());
        holder.post.setText(c.getComentario());

        String[] temp = c.getData().split(" ");
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataForm = sdf.format(date);

        if (temp[0].equals(dataForm)) {
            temp = temp[1].split(":");
            holder.data.setText("às " + temp[0] + ":" + temp[1]);
        }else{
            temp = temp[0].split("-");
            holder.data.setText("em " + temp[2] + "/" + temp[1]);
        }

        if (c.getCurtidaComentario() != null)
            for (int x=0; x < c.getCurtidaComentario().length; x++) {
                if (c.getCurtidaComentario()[x].getUsuario() == DadosUsuario.codigo){
                    holder.addOne.setBackgroundResource(R.drawable.ic_added);
                }
            }

        holder.addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addOne.setImageResource(R.drawable.ic_added);
                //Toast.makeText(contexto, "add one.", Toast.LENGTH_LONG).show();
            }
        });

        if (c.getCurtidaComentario() != null)
            holder.qtdAddOne.setText(String.valueOf(c.getCurtidaComentario().length) + " curtiu");
        else
            holder.qtdAddOne.setText("");

    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

}
