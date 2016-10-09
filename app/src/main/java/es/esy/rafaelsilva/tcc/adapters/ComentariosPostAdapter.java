package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.ComentarioPost;
import es.esy.rafaelsilva.tcc.util.Config;

/**
 * Created by Rafael on 08/09/2016.
 */
public class ComentariosPostAdapter extends RecyclerView.Adapter<ComentariosPostAdapter.MyViewHolder> {

    private List<ComentarioPost> comentarios;
    private Context contexto;

    public ComentariosPostAdapter(List<ComentarioPost> comentarios, Context contexto) {
        this.comentarios = comentarios;
        this.contexto = contexto;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nome, post, data;
        public CircleImageView imgUsuario;

        public MyViewHolder(View v) {
            super(v);
            nome = (TextView) v.findViewById(R.id.lbNome);
            post = (TextView) v.findViewById(R.id.lbPost);
            data = (TextView) v.findViewById(R.id.lbData);
            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comentario_post, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final ComentarioPost c = comentarios.get(position);
        ImageLoaderTask downImg = new ImageLoaderTask(holder.imgUsuario);
        downImg.execute(Config.caminhoImageTumb + c.getUsoarioObj().getImagem());

        holder.post.setText(c.getComentario());
        holder.nome.setText(c.getUsoarioObj().getNome());

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
    }


    @Override
    public int getItemCount() {
        return comentarios.size();
    }


}
