package es.esy.rafaelsilva.tcc.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.esy.rafaelsilva.tcc.activity.ComentariosPostActivity;
import es.esy.rafaelsilva.tcc.task.ImageLoaderTask;
import es.esy.rafaelsilva.tcc.task.UtilTask;
import es.esy.rafaelsilva.tcc.R;
import es.esy.rafaelsilva.tcc.modelo.Comentario;
import es.esy.rafaelsilva.tcc.modelo.CurtidaComentario;
import es.esy.rafaelsilva.tcc.util.Config;
import es.esy.rafaelsilva.tcc.util.DadosUsuario;

/**
 * Created by Rafael on 07/09/2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Comentario> comentarios;
    private Context contexto;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public boolean curtido = false;
        public TextView nome, post, data, qtdAddOne, numComent;
        public CircleImageView imgUsuario;
        public ImageView addOne, coment;

        public MyViewHolder(View v) {
            super(v);
            imgUsuario = (CircleImageView) v.findViewById(R.id.imgUsuario);
            nome = (TextView) v.findViewById(R.id.lbNome);
            post = (TextView) v.findViewById(R.id.lbPost);
            data = (TextView) v.findViewById(R.id.lbData);
            addOne = (ImageView) v.findViewById(R.id.imgAddOne);
            coment = (ImageView) v.findViewById(R.id.imgComentarios);
            qtdAddOne = (TextView) v.findViewById(R.id.lbAddOne);
            numComent = (TextView) v.findViewById(R.id.lbComentarios);
        }
    }


    public PostAdapter(List<Comentario> comentarios, Context contexto) {
        this.comentarios = comentarios;
        this.contexto = contexto;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflater_post, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        int curtido = 0;
        final Comentario c = comentarios.get(position);
        ImageLoaderTask downImg = new ImageLoaderTask(holder.imgUsuario);
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
            for (int i=0; i < c.getCurtidaComentario().length; i++) {
                CurtidaComentario cc = c.getCurtidaComentario()[i];
                if (cc.getUsuario() == DadosUsuario.codigo){
                    holder.addOne.setImageResource(R.drawable.ic_added);
                    i = c.getCurtidaComentario().length;
                    holder.curtido = true;
                    curtido = 1;
                }
            }

        if (c.getCurtidaComentario() != null) {
            holder.qtdAddOne.setText(String.valueOf(c.getCurtidaComentario().length) + " curtiu");
        }else {
            holder.qtdAddOne.setText("");
        }

        // exclui curtida
        holder.addOne.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                if (holder.curtido) {
                    UtilTask util = new UtilTask(view.getContext(), "D", "curtidacomentario");
                    util.execute("usuario", String.valueOf(DadosUsuario.codigo) + " AND comentario = " + c.getCodigo());

                    holder.addOne.setImageResource(R.drawable.ic_add_one);

                    int curtiu = 0;
                    if (c.getCurtidaComentario() != null)
                        curtiu = c.getCurtidaComentario().length - 1;

                    holder.qtdAddOne.setText(String.valueOf(curtiu) + " curtiu");

                    holder.curtido = false;
                }

                //Toast.makeText(view.getContext(), "long "+String.valueOf(holder.curtido), Toast.LENGTH_LONG).show();
                return true;

            }
        });

        //add curtida
        final int finalCurtido = curtido;
        holder.addOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!holder.curtido) {
                    UtilTask util = new UtilTask(view.getContext(), "C", "curtidacomentario");
                    String campos = "comentario,usuario";
                    String values = c.getCodigo() + "," + DadosUsuario.codigo;
                    util.execute(campos, values);

                    holder.addOne.setImageResource(R.drawable.ic_added);

                    int curtiu = 0;
                    if (c.getCurtidaComentario() != null)
                        curtiu = c.getCurtidaComentario().length + 1 - finalCurtido;
                    else
                        curtiu = 1;

                    holder.qtdAddOne.setText(String.valueOf(curtiu) + " você curtiu");

                    holder.curtido = true;

                }

                //Toast.makeText(view.getContext(), "curto "+String.valueOf(holder.curtido), Toast.LENGTH_LONG).show();

            }
        });



        if (c.getComentariosPost() != null){
            holder.coment.setImageResource(R.drawable.ic_comented);
            holder.numComent.setText(String.valueOf(c.getComentariosPost().length) + " comentou");
        }else{
            holder.numComent.setText("");
        }


        holder.coment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(contexto, ComentariosPostActivity.class);
                intent.putExtra("post", c.getCodigo());
                contexto.startActivity(intent);

            }
        });

    }



    @Override
    public int getItemCount() {
        return comentarios.size();
    }


}
